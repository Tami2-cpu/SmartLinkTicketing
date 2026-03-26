package com.zhilian.captcha.service;

import java.util.Map;

public interface CaptchaService {
    /**
     * 生成验证码
     * @return 包含验证码ID和图片base64的Map
     */
    Map<String, Object> generateCaptcha();

    /**
     * 验证验证码
     * @param captchaId 验证码ID
     * @param captcha 验证码值
     * @return 是否验证成功
     */
    boolean verifyCaptcha(String captchaId, String captcha);
}