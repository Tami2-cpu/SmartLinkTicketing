package com.zhilian.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhilian.core.bloomfilter.BloomFilterHandler;
import com.zhilian.core.composite.CompositeContainer;
import com.zhilian.core.exception.BusinessException;
import com.zhilian.core.lock.LockType;
import com.zhilian.core.lock.ServiceLock;
import com.zhilian.user.dto.UserLoginDTO;
import com.zhilian.user.dto.UserRegisterDTO;
import com.zhilian.user.entity.User;
import com.zhilian.user.mapper.UserMapper;
import com.zhilian.user.service.UserService;
import com.zhilian.user.vo.UserVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Integer ERROR_COUNT_THRESHOLD = 5;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private BloomFilterHandler bloomFilterHandler;

    @Resource
    private CompositeContainer compositeContainer;

    /**
     * 用户注册
     * 
     * @param userDTO 用户注册信息
     * @return 注册后的用户信息
     */
    @Override
    @ServiceLock(lockType = LockType.Write, name = "register_user_lock", keys = { "#userDTO.mobile" })
    public UserVO register(UserRegisterDTO userDTO) {
        // 执行复合校验
        compositeContainer.execute(userDTO);

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

        // 将手机号添加到布隆过滤器
        if (user.getMobile() != null) {
            bloomFilterHandler.add(user.getMobile());
        }

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
        String username = userLoginDTO.getUsername();

        // 检查登录错误次数
        String errorCountKey = "login_error_count:" + username;
        Object errorCountObj = redisTemplate.opsForValue().get(errorCountKey);
        if (errorCountObj != null) {
            int errorCount = Integer.parseInt(errorCountObj.toString());
            if (errorCount >= ERROR_COUNT_THRESHOLD) {
                throw BusinessException.unauthorized("登录错误次数过多，请稍后重试");
            }
        }

        User user = getByUsername(username);
        if (user == null) {
            // 记录错误次数
            redisTemplate.opsForValue().increment(errorCountKey);
            redisTemplate.expire(errorCountKey, 1, java.util.concurrent.TimeUnit.MINUTES);
            throw BusinessException.unauthorized("用户名或密码错误");
        }
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            // 记录错误次数
            redisTemplate.opsForValue().increment(errorCountKey);
            redisTemplate.expire(errorCountKey, 1, java.util.concurrent.TimeUnit.MINUTES);
            throw BusinessException.unauthorized("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw BusinessException.forbidden("账号已被禁用");
        }

        // 登录成功，清除错误次数
        redisTemplate.delete(errorCountKey);

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
        // 先从布隆过滤器检查是否存在
        if (!bloomFilterHandler.contains(mobile)) {
            return null;
        }

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

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw BusinessException.badRequest("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);

        // 清理缓存
        clearUserCache(user.getId(), user.getUsername(), user.getMobile(), user.getEmail());
    }

    @Override
    public void updateEmail(Long userId, String email) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 检查邮箱是否已被使用
        if (getByEmail(email) != null) {
            throw BusinessException.badRequest("邮箱已被使用");
        }

        // 保存旧值用于缓存清理
        String oldEmail = user.getEmail();

        // 更新邮箱
        user.setEmail(email);
        updateById(user);

        // 清理旧缓存
        clearUserCache(user.getId(), user.getUsername(), user.getMobile(), oldEmail);

        // 存入新缓存
        putUserToCache(user);
    }

    @Override
    public void updateMobile(Long userId, String mobile) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 检查手机号是否已被使用
        if (getByMobile(mobile) != null) {
            throw BusinessException.badRequest("手机号已被使用");
        }

        // 保存旧值用于缓存清理
        String oldMobile = user.getMobile();

        // 更新手机号
        user.setMobile(mobile);
        updateById(user);

        // 清理旧缓存
        clearUserCache(user.getId(), user.getUsername(), oldMobile, user.getEmail());

        // 存入新缓存
        putUserToCache(user);

        // 更新布隆过滤器
        if (oldMobile != null) {
            // 注意：布隆过滤器不支持删除操作，这里只添加新手机号
        }
        bloomFilterHandler.add(mobile);
    }

    @Override
    public void submitAuthentication(Long userId, String realName, String idCard) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }

        // 验证身份证号码格式
        if (!idCard
                .matches("^[1-9][0-9]{5}(18|19|20)[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9Xx]$") ){
            throw BusinessException.badRequest("身份证号码格式错误");
        }

        // 清理缓存
        clearUserCache(user.getId(), user.getUsername(), user.getMobile(), user.getEmail());
    }

    @Override
    public Integer getAuthStatus(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        return 0; // 默认返回0，表示未认证
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
            logger.error("从缓存获取用户信息失败: {}", e.getMessage(), e);
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
            logger.error("将用户信息存入缓存失败: {}", e.getMessage(), e);
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
            logger.error("清理用户缓存失败: {}", e.getMessage(), e);
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