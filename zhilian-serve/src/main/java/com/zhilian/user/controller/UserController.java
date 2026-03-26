package com.zhilian.user.controller;

import com.zhilian.core.exception.Result;

import com.zhilian.core.utils.JwtUtil;
import com.zhilian.core.utils.UserActionLogger;
import com.zhilian.core.utils.UserContext;
import com.zhilian.image.service.ImageService;
import com.zhilian.user.dto.UserLoginDTO;
import com.zhilian.user.dto.UserRegisterDTO;
import com.zhilian.user.entity.User;
import com.zhilian.user.service.UserService;
import com.zhilian.user.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户模块")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final ImageService imageService;

    public UserController(UserService userService, JwtUtil jwtUtil, ImageService imageService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.imageService = imageService;
    }

    /**
     * 用户注册
     * 
     * @param userDTO 用户注册信息
     * @return 注册结果
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<UserVO> register(@Valid @RequestBody UserRegisterDTO userDTO) {
        return Result.success(userService.register(userDTO));
    }

    /**
     * 用户登录
     * 
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<Map<String, Object>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        UserVO user = userService.login(loginDTO);

        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        String token = jwtUtil.generateToken(user.getUsername(), claims);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        return Result.success(result);
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result<UserVO> getInfo() {
        logger.info("Getting user info");

        // 从ThreadLocal中获取当前登录用户
        User user = UserContext.getUser();
        logger.info("User from UserContext: {}", user != null ? user.getUsername() : "null");

        if (user != null) {
            // 创建并返回UserVO
            logger.info("Returning user info from UserContext");
            return Result.success(convertToVO(user));
        }

        // 如果ThreadLocal中没有用户信息，尝试从SecurityContext中获取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authentication from SecurityContext: {}", authentication);

        if (authentication != null) {
            logger.info("Authentication is authenticated: {}", authentication.isAuthenticated());
            logger.info("Authentication principal: {}", authentication.getPrincipal());

            if (authentication.isAuthenticated() && authentication.getPrincipal() != null) {
                String username = authentication.getName();
                logger.info("Username from authentication: {}", username);

                user = userService.getByUsername(username);
                logger.info("User from database: {}", user != null ? user.getUsername() : "null");

                if (user != null) {
                    logger.info("Returning user info from database");
                    return Result.success(convertToVO(user));
                }
            }
        }

        logger.info("No user found, returning unauthorized");
        return Result.error(Result.UNAUTHORIZED, "未登录");
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
     * 更新用户信息
     * 
     * @param userVO 用户信息
     * @return 更新结果
     */
    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public Result<UserVO> update(@RequestBody UserVO userVO) {
        return Result.success(userService.update(userVO));
    }

    /**
     * 发送重置密码验证码
     * 
     * @param emailOrMobile 邮箱或手机号
     * @return 发送结果
     */
    @PostMapping("/send-reset-code")
    @ApiOperation("发送重置密码验证码")
    public Result<String> sendResetCode(@RequestParam String emailOrMobile) {
        userService.sendResetCode(emailOrMobile);
        return Result.success("验证码发送成功");
    }

    /**
     * 重置密码
     * 
     * @param emailOrMobile 邮箱或手机号
     * @param code          验证码
     * @param newPassword   新密码
     * @return 重置结果
     */
    @PostMapping("/reset-password")
    @ApiOperation("重置密码")
    public Result<String> resetPassword(@RequestParam String emailOrMobile, @RequestParam String code,
            @RequestParam String newPassword) {
        userService.resetPassword(emailOrMobile, code, newPassword);
        return Result.success("密码重置成功");
    }

    /**
     * 上传头像
     * 
     * @param file 头像文件
     * @return 上传结果
     */
    @PostMapping("/upload-avatar")
    @ApiOperation("上传头像")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 从ThreadLocal中获取当前登录用户
            User user = UserContext.getUser();
            if (user != null) {
                // 调用图片服务上传头像
                String avatarUrl = imageService.uploadImage(file.getBytes(), file.getOriginalFilename());
                // 更新用户头像
                user.setAvatar(avatarUrl);
                userService.updateById(user);
                return Result.success(avatarUrl);
            }
            return Result.error(Result.UNAUTHORIZED, "未登录");
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }
}