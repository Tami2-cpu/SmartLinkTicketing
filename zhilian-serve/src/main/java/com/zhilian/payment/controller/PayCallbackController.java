package com.zhilian.payment.controller;

import com.zhilian.core.exception.Result;
import com.zhilian.payment.dto.NotifyDTO;
import com.zhilian.payment.service.PayService;
import com.zhilian.payment.vo.NotifyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 支付回调控制器
 * 
 * @author 智联票务技术团队
 * @date 2026-04-01
 */
@RestController
@RequestMapping("/api/pay/callback")
@Api(tags = "支付回调模块")
public class PayCallbackController {

    @Resource
    private PayService payService;

    /**
     * 通用支付回调
     * 
     * @param notifyDTO 回调信息
     * @return 回调结果
     */
    @PostMapping("/common")
    @ApiOperation("通用支付回调")
    public Result<NotifyVO> commonCallback(@RequestBody NotifyDTO notifyDTO) {
        return Result.success(payService.notify(notifyDTO));
    }

    /**
     * 微信支付回调
     * 
     * @param callbackData 回调数据
     * @return 回调结果
     */
    @PostMapping("/wx")
    @ApiOperation("微信支付回调")
    public Result wxCallback(@RequestBody String callbackData) {
        // 这里可以根据实际情况进行处理
        return Result.success("处理成功");
    }

    /**
     * 支付宝支付回调
     * 
     * @param callbackData 回调数据
     * @return 回调结果
     */
    @PostMapping("/alipay")
    @ApiOperation("支付宝支付回调")
    public Result alipayCallback(@RequestBody String callbackData) {
        // 这里可以根据实际情况进行处理
        return Result.success("处理成功");
    }
}