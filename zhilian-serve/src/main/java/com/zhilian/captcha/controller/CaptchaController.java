package com.zhilian.captcha.controller;

import com.zhilian.captcha.service.CaptchaService;
import com.zhilian.core.exception.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/captcha")
@Api(tags = "图形验证码模块")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/generate")
    @ApiOperation("生成图形验证码")
    public Result<Map<String, Object>> generate() {
        Map<String, Object> result = captchaService.generateCaptcha();
        return Result.success(result);
    }

    @PostMapping("/verify")
    @ApiOperation("验证图形验证码")
    public Result<String> verify(@RequestBody Map<String, String> verifyInfo) {
        String captchaId = verifyInfo.get("captchaId");
        String captcha = verifyInfo.get("captcha");

        boolean success = captchaService.verifyCaptcha(captchaId, captcha);
        if (success) {
            return Result.success("验证成功");
        } else {
            return Result.error("验证码错误");
        }
    }
}