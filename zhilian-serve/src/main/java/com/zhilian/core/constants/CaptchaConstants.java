package com.zhilian.core.constants;

/**
 * 验证码相关常量
 * 
 * @author 智联票务技术团队
 * @date 2026-03-25
 */
public class CaptchaConstants {

    // 验证码过期时间（秒）
    public static final long CAPTCHA_EXPIRE_TIME = 5 * 60;

    // 验证码缓存键前缀
    public static final String CAPTCHA_KEY_PREFIX = "captcha:";
}
