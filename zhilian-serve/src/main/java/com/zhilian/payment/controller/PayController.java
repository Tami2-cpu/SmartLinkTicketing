package com.zhilian.payment.controller;

import com.zhilian.core.exception.Result;
import com.zhilian.payment.service.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/pay")
@Api(tags = "支付模块")
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping("/wx")
    @ApiOperation("微信支付")
    public Result wxPay(@RequestBody Map<String, Object> payInfo) {
        Long orderId = (Long) payInfo.get("orderId");
        Double amount = (Double) payInfo.get("amount");
        String payUrl = payService.createWxPay(orderId, amount);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("payUrl", payUrl);
        return Result.success(result);
    }

    @PostMapping("/alipay")
    @ApiOperation("支付宝支付")
    public Result alipay(@RequestBody Map<String, Object> payInfo) {
        Long orderId = (Long) payInfo.get("orderId");
        Double amount = (Double) payInfo.get("amount");
        String payUrl = payService.createAlipay(orderId, amount);
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("payUrl", payUrl);
        return Result.success(result);
    }

    @GetMapping("/result")
    @ApiOperation("查询支付结果")
    public Result getPayResult(@RequestParam Long orderId) {
        return Result.success(payService.getPayResult(orderId));
    }
}