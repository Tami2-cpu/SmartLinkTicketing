package com.zhilian.core.utils;

import com.zhilian.user.entity.User;

/**
 * 用户上下文工具类，用于存储和获取当前登录用户信息
 * 
 * @author 智联票务技术团队
 * @date 2026-03-25
 */
public class UserContext {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
    
    /**
     * 设置当前登录用户
     * 
     * @param user 用户信息
     */
    public static void setUser(User user) {
        userThreadLocal.set(user);
    }
    
    /**
     * 获取当前登录用户
     * 
     * @return 用户信息
     */
    public static User getUser() {
        return userThreadLocal.get();
    }
    
    /**
     * 清理当前登录用户
     */
    public static void clear() {
        userThreadLocal.remove();
    }
}
