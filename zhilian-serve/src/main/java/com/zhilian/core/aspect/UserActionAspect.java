package com.zhilian.core.aspect;

import com.zhilian.core.utils.UserActionLogger;
import com.zhilian.core.utils.UserContext;
import com.zhilian.user.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 用户行为切面，用于自动记录用户操作日志
 * 
 * @author 智联票务技术团队
 * @date 2026-03-25
 */
@Aspect
@Component
public class UserActionAspect {

    /**
     * 定义切入点，拦截用户控制器中的方法
     */
    @Pointcut("execution(* com.zhilian.user.controller.UserController.*(..))")
    public void userActionPointcut() {}

    /**
     * 在方法执行前记录用户操作日志
     * 
     * @param joinPoint 连接点
     */
    @Before("userActionPointcut()")
    public void beforeUserAction(JoinPoint joinPoint) {
        // 获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 获取客户端IP地址
        String ip = request.getRemoteAddr();
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        
        // 根据方法名记录不同的操作日志
        switch (methodName) {
            case "register":
                // 注册操作
                if (args.length > 0) {
                    // 假设第一个参数是UserRegisterDTO
                    Object userDTO = args[0];
                    try {
                        // 使用反射获取username字段
                        String username = (String) userDTO.getClass().getMethod("getUsername").invoke(userDTO);
                        UserActionLogger.logRegister(username, true, ip);
                    } catch (Exception e) {
                        // 反射失败，使用默认值
                        UserActionLogger.logRegister("unknown", true, ip);
                    }
                }
                break;
            case "login":
                // 登录操作
                if (args.length > 0) {
                    // 假设第一个参数是UserLoginDTO
                    Object loginDTO = args[0];
                    try {
                        // 使用反射获取username字段
                        String username = (String) loginDTO.getClass().getMethod("getUsername").invoke(loginDTO);
                        UserActionLogger.logLogin(username, true, ip);
                    } catch (Exception e) {
                        // 反射失败，使用默认值
                        UserActionLogger.logLogin("unknown", true, ip);
                    }
                }
                break;
            case "update":
                // 更新操作
                User user = UserContext.getUser();
                if (user != null) {
                    UserActionLogger.logUserUpdate(user.getUsername(), true, ip);
                } else {
                    UserActionLogger.logUserUpdate("anonymous", true, ip);
                }
                break;
            case "sendResetCode":
                // 发送重置密码验证码操作
                if (args.length > 0) {
                    // 假设第一个参数是emailOrMobile
                    String emailOrMobile = (String) args[0];
                    UserActionLogger.logPasswordReset(emailOrMobile, true, ip);
                }
                break;
            case "resetPassword":
                // 重置密码操作
                if (args.length > 0) {
                    // 假设第一个参数是emailOrMobile
                    String emailOrMobile = (String) args[0];
                    UserActionLogger.logPasswordReset(emailOrMobile, true, ip);
                }
                break;
            case "uploadAvatar":
                // 上传头像操作
                user = UserContext.getUser();
                if (user != null) {
                    UserActionLogger.logAvatarUpload(user.getUsername(), true, ip);
                } else {
                    UserActionLogger.logAvatarUpload("anonymous", true, ip);
                }
                break;
            default:
                // 其他操作
                break;
        }
    }
}
