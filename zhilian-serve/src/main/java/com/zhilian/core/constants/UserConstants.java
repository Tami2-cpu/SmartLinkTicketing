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
    // 登录失败缓存过期时间：1小时
    public static final long LOGIN_FAIL_EXPIRATION = 3600;
    // 验证码过期时间：5分钟
    public static final long VERIFICATION_CODE_EXPIRATION = 300;

    // 缓存键前缀
    public static final String USER_KEY_PREFIX = "user:";
    public static final String USER_USERNAME_PREFIX = "user:username:";
    public static final String USER_MOBILE_PREFIX = "user:mobile:";
    public static final String USER_EMAIL_PREFIX = "user:email:";
    public static final String LOGIN_FAIL_PREFIX = "login:fail:";
    public static final String RESET_CODE_PREFIX = "reset:code:";
    public static final String VERIFICATION_CODE_PREFIX = "verification:code:";

    // 用户状态
    public static final Integer USER_STATUS_ACTIVE = 1;
    public static final Integer USER_STATUS_INACTIVE = 0;

    // 密码相关
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 20;

    // 登录失败次数限制
    public static final int LOGIN_FAIL_MAX_COUNT = 5;

    // JWT相关
    public static final String JWT_CLAIM_USER_ID = "userId";
    public static final String JWT_CLAIM_USERNAME = "username";
    public static final long JWT_EXPIRATION = 86400; // 24小时

    // 验证码长度
    public static final int VERIFICATION_CODE_LENGTH = 6;
    
    // 购票人相关
    public static final String TICKET_USER_PREFIX = "ticket:user:";
    public static final String USER_TICKET_USERS_PREFIX = "user:ticket:users:";
    
    // 布隆过滤器相关
    public static final String USER_MOBILE_BLOOM_FILTER = "user:mobile:bloom:filter";
    public static final long BLOOM_FILTER_EXPECTED_ELEMENTS = 1000000;
    public static final double BLOOM_FILTER_FPP = 0.01;
    
    // 分布式锁相关
    public static final String REGISTER_USER_LOCK = "lock:register:user";
    public static final long LOCK_EXPIRE_TIME = 30; // 锁过期时间（秒）
    public static final long LOCK_WAIT_TIME = 10; // 等待获取锁的时间（秒）
}
