package com.zhilian.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.core.exception.BusinessException;
import com.zhilian.user.dto.UserLoginDTO;
import com.zhilian.user.dto.UserRegisterDTO;
import com.zhilian.user.entity.User;
import com.zhilian.user.mapper.UserMapper;
import com.zhilian.user.service.UserService;
import com.zhilian.user.vo.UserVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.zhilian.core.constants.UserConstants;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册
     * 
     * @param userDTO 用户注册信息
     * @return 注册后的用户信息
     */
    @Override
    public UserVO register(UserRegisterDTO userDTO) {
        // 检查用户名是否已存在
        if (getByUsername(userDTO.getUsername()) != null) {
            throw BusinessException.badRequest("用户名已存在");
        }
        // 检查手机号是否已存在
        if (getByMobile(userDTO.getMobile()) != null) {
            throw BusinessException.badRequest("手机号已存在");
        }
        // 检查邮箱是否已存在
        if (userDTO.getEmail() != null && getByEmail(userDTO.getEmail()) != null) {
            throw BusinessException.badRequest("邮箱已存在");
        }

        // 创建用户实体
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setMobile(userDTO.getMobile());
        user.setEmail(userDTO.getEmail());
        user.setNickname(userDTO.getNickname());
        user.setStatus(1);

        // 保存用户
        save(user);

        // 转换为VO
        return convertToVO(user);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 登录后的用户信息
     */
    @Override
    public UserVO login(UserLoginDTO userLoginDTO) {
        User user = getByUsername(userLoginDTO.getUsername());
        if (user == null) {
            throw BusinessException.unauthorized("用户名或密码错误");
        }
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw BusinessException.unauthorized("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw BusinessException.forbidden("账号已被禁用");
        }

        // 转换为VO
        return convertToVO(user);
    }

    /**
     * 根据用户名获取用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User getByUsername(String username) {
        // 先从缓存获取
        String cacheKey = UserConstants.USER_USERNAME_PREFIX + username;
        User user = getFromCache(cacheKey);
        if (user != null) {
            return user;
        }

        // 缓存未命中，从数据库查询
        user = lambdaQuery().eq(User::getUsername, username).one();
        if (user != null) {
            // 存入缓存
            putToCache(cacheKey, user);
            putToCache(UserConstants.USER_KEY_PREFIX + user.getId(), user);
            if (user.getMobile() != null) {
                putToCache(UserConstants.USER_MOBILE_PREFIX + user.getMobile(), user);
            }
            if (user.getEmail() != null) {
                putToCache(UserConstants.USER_EMAIL_PREFIX + user.getEmail(), user);
            }
        }
        return user;
    }

    /**
     * 根据手机号获取用户
     * 
     * @param mobile 手机号
     * @return 用户信息
     */
    @Override
    public User getByMobile(String mobile) {
        // 先从缓存获取
        String cacheKey = UserConstants.USER_MOBILE_PREFIX + mobile;
        User user = getFromCache(cacheKey);
        if (user != null) {
            return user;
        }

        // 缓存未命中，从数据库查询
        user = lambdaQuery().eq(User::getMobile, mobile).one();
        if (user != null) {
            // 存入缓存
            putToCache(cacheKey, user);
            putToCache(UserConstants.USER_KEY_PREFIX + user.getId(), user);
            putToCache(UserConstants.USER_USERNAME_PREFIX + user.getUsername(), user);
            if (user.getEmail() != null) {
                putToCache(UserConstants.USER_EMAIL_PREFIX + user.getEmail(), user);
            }
        }
        return user;
    }

    /**
     * 根据邮箱获取用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    @Override
    public User getByEmail(String email) {
        // 先从缓存获取
        String cacheKey = UserConstants.USER_EMAIL_PREFIX + email;
        User user = getFromCache(cacheKey);
        if (user != null) {
            return user;
        }

        // 缓存未命中，从数据库查询
        user = lambdaQuery().eq(User::getEmail, email).one();
        if (user != null) {
            // 存入缓存
            putToCache(cacheKey, user);
            putToCache(UserConstants.USER_KEY_PREFIX + user.getId(), user);
            putToCache(UserConstants.USER_USERNAME_PREFIX + user.getUsername(), user);
            if (user.getMobile() != null) {
                putToCache(UserConstants.USER_MOBILE_PREFIX + user.getMobile(), user);
            }
        }
        return user;
    }

    /**
     * 发送重置密码验证码
     * 
     * @param emailOrMobile 邮箱或手机号
     */
    @Override
    public void sendResetCode(String emailOrMobile) {
        // 生成6位验证码
        String code = generateRandomCode(6);
        // 检查用户是否存在
        User user = null;
        if (emailOrMobile.contains("@")) {
            user = getByEmail(emailOrMobile);
        } else {
            user = getByMobile(emailOrMobile);
        }
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        // 存储验证码到Redis，有效期5分钟
        redisTemplate.opsForValue().set("reset_code:" + emailOrMobile, code, 5, TimeUnit.MINUTES);
        // 模拟发送邮件或短信
        System.out.println("发送重置密码验证码: " + code + " 到 " + emailOrMobile);
    }

    /**
     * 重置密码
     * 
     * @param emailOrMobile 邮箱或手机号
     * @param code          验证码
     * @param newPassword   新密码
     */
    @Override
    public void resetPassword(String emailOrMobile, String code, String newPassword) {
        // 验证验证码
        Object storedCodeObj = redisTemplate.opsForValue().get("reset_code:" + emailOrMobile);
        String storedCode = storedCodeObj != null ? storedCodeObj.toString() : null;
        if (storedCode == null || !storedCode.equals(code)) {
            throw BusinessException.badRequest("验证码错误或已过期");
        }
        // 查找用户
        User user = null;
        if (emailOrMobile.contains("@")) {
            user = getByEmail(emailOrMobile);
        } else {
            user = getByMobile(emailOrMobile);
        }
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
        // 删除验证码
        redisTemplate.delete("reset_code:" + emailOrMobile);
    }

    /**
     * 根据ID获取用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public User getById(Serializable id) {
        // 先从缓存获取
        String cacheKey = UserConstants.USER_KEY_PREFIX + id;
        User user = getFromCache(cacheKey);
        if (user != null) {
            return user;
        }

        // 缓存未命中，从数据库查询
        user = super.getById(id);
        if (user != null) {
            // 存入缓存
            putToCache(cacheKey, user);
            putToCache(UserConstants.USER_USERNAME_PREFIX + user.getUsername(), user);
            if (user.getMobile() != null) {
                putToCache(UserConstants.USER_MOBILE_PREFIX + user.getMobile(), user);
            }
            if (user.getEmail() != null) {
                putToCache(UserConstants.USER_EMAIL_PREFIX + user.getEmail(), user);
            }
        }
        return user;
    }

    /**
     * 更新用户信息
     * 
     * @param userVO 用户信息
     * @return 更新后的用户信息
     */
    @Override
    public UserVO update(UserVO userVO) {
        User user = getById(userVO.getId());
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 保存旧值用于缓存清理
        String oldUsername = user.getUsername();
        String oldMobile = user.getMobile();
        String oldEmail = user.getEmail();

        // 更新用户信息
        user.setNickname(userVO.getNickname());
        user.setMobile(userVO.getMobile());
        user.setEmail(userVO.getEmail());
        user.setAvatar(userVO.getAvatar());
        user.setStatus(userVO.getStatus());

        updateById(user);

        // 清理旧缓存
        clearUserCache(user.getId(), oldUsername, oldMobile, oldEmail);

        // 存入新缓存
        putUserToCache(user);

        // 转换为VO
        return convertToVO(user);
    }

    /**
     * 生成随机验证码
     * 
     * @param length 验证码长度
     * @return 随机验证码
     */
    private String generateRandomCode(int length) {
        String chars = "0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    /**
     * 将User实体转换为UserVO
     * 
     * @param user 用户实体
     * @return 用户VO
     */
    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setMobile(user.getMobile());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setStatus(user.getStatus());
        userVO.setCreateTime(user.getCreateTime());
        userVO.setUpdateTime(user.getUpdateTime());
        return userVO;
    }

    /**
     * 从缓存获取用户信息
     * 
     * @param key 缓存键
     * @return 用户信息
     */
    private User getFromCache(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                return (User) value;
            }
        } catch (Exception e) {
            // 缓存读取失败，返回null，从数据库查询
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将用户信息存入缓存
     * 
     * @param key  缓存键
     * @param user 用户信息
     */
    private void putToCache(String key, User user) {
        try {
            redisTemplate.opsForValue().set(key, user, UserConstants.CACHE_EXPIRATION, TimeUnit.SECONDS);
        } catch (Exception e) {
            // 缓存写入失败，忽略异常
            e.printStackTrace();
        }
    }

    /**
     * 清理用户缓存
     * 
     * @param id       用户ID
     * @param username 用户名
     * @param mobile   手机号
     * @param email    邮箱
     */
    private void clearUserCache(Long id, String username, String mobile, String email) {
        try {
            redisTemplate.delete(UserConstants.USER_KEY_PREFIX + id);
            redisTemplate.delete(UserConstants.USER_USERNAME_PREFIX + username);
            if (mobile != null) {
                redisTemplate.delete(UserConstants.USER_MOBILE_PREFIX + mobile);
            }
            if (email != null) {
                redisTemplate.delete(UserConstants.USER_EMAIL_PREFIX + email);
            }
        } catch (Exception e) {
            // 缓存清理失败，忽略异常
            e.printStackTrace();
        }
    }

    /**
     * 将用户信息存入所有相关缓存
     * 
     * @param user 用户信息
     */
    private void putUserToCache(User user) {
        putToCache(UserConstants.USER_KEY_PREFIX + user.getId(), user);
        putToCache(UserConstants.USER_USERNAME_PREFIX + user.getUsername(), user);
        if (user.getMobile() != null) {
            putToCache(UserConstants.USER_MOBILE_PREFIX + user.getMobile(), user);
        }
        if (user.getEmail() != null) {
            putToCache(UserConstants.USER_EMAIL_PREFIX + user.getEmail(), user);
        }
    }
}