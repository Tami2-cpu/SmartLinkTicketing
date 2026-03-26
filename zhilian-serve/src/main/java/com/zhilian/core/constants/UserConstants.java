package com.zhilian.core.constants;

/**
 * 用户相关常量
 * 
 * @author 智联票务技术团队
 * @date 2026-03-25
 */
public class UserConstants {

    // 缓存过期时间：1小时
    public static final long CACHE_EXPIRATION = 3600;

    // 缓存键前缀
    public static final String USER_KEY_PREFIX = "user:";
    public static final String USER_USERNAME_PREFIX = "user:username:";
    public static final String USER_MOBILE_PREFIX = "user:mobile:";
    public static final String USER_EMAIL_PREFIX = "user:email:";
}
