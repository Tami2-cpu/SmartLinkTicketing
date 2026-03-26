package com.zhilian.payment.controller;

import com.zhilian.core.exception.Result;
import com.zhilian.payment.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/pay/callback")
@Api(tags = "支付回调模块")
public class PayCallbackController {

    @Resource
    private PayService payService;

    @PostMapping("/wx")
    @ApiOperation("微信支付回调")
    public Result wxCallback(@RequestBody String callbackData) {
        boolean result = payService.handleWxCallback(callbackData);
        if (result) {
            return Result.success("处理成功");
        } else {
            return Result.error("处理失败");
        }
    }

    @PostMapping("/alipay")
    @ApiOperation("支付宝支付回调")
    public Result alipayCallback(@RequestBody String callbackData) {
        boolean result = payService.handleAlipayCallback(callbackData);
        if (result) {
            return Result.success("处理成功");
        } else {
            return Result.error("处理失败");
        }
    }
}