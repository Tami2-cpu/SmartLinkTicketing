package com.zhilian.captcha.service.impl;

import com.zhilian.captcha.service.CaptchaService;
import com.zhilian.core.constants.CaptchaConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> generateCaptcha() {
        // 生成随机验证码
        String captchaCode = generateRandomCode(4);
        // 生成验证码ID
        String captchaId = UUID.randomUUID().toString();
        // 生成验证码图片
        String imageBase64 = generateCaptchaImage(captchaCode);

        // 存储验证码到Redis，设置过期时间
        redisTemplate.opsForValue().set(CaptchaConstants.CAPTCHA_KEY_PREFIX + captchaId, captchaCode,
                CaptchaConstants.CAPTCHA_EXPIRE_TIME, TimeUnit.SECONDS);

        Map<String, Object> result = new HashMap<>();
        result.put("captchaId", captchaId);
        result.put("imageBase64", imageBase64);
        return result;
    }

    @Override
    public boolean verifyCaptcha(String captchaId, String captcha) {
        if (captchaId == null || captcha == null) {
            return false;
        }

        String storedCaptcha = redisTemplate.opsForValue().get(CaptchaConstants.CAPTCHA_KEY_PREFIX + captchaId);
        if (storedCaptcha == null) {
            return false;
        }

        boolean result = storedCaptcha.equalsIgnoreCase(captcha);
        // 验证后移除验证码
        redisTemplate.delete(CaptchaConstants.CAPTCHA_KEY_PREFIX + captchaId);
        return result;
    }

    /**
     * 生成随机验证码
     * 
     * @param length 验证码长度
     * @return 随机验证码
     */
    private String generateRandomCode(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    /**
     * 生成验证码图片
     * 
     * @param code 验证码
     * @return base64格式的图片
     */
    private String generateCaptchaImage(String code) {
        int width = 120;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置边框
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, width - 1, height - 1);

        // 生成干扰线
        Random random = new Random();
        g.setColor(Color.GRAY);
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制验证码
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < code.length(); i++) {
            // 随机位置和角度
            int x = 20 + i * 20;
            int y = 28 + random.nextInt(6) - 3;
            double angle = (random.nextInt(20) - 10) * Math.PI / 180;
            g.rotate(angle, x, y);
            g.drawString(String.valueOf(code.charAt(i)), x, y);
            g.rotate(-angle, x, y);
        }

        g.dispose();

        // 转换为base64
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}