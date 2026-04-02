package com.zhilian.payment.controller;

import com.zhilian.core.exception.Result;
import com.zhilian.payment.dto.PayDTO;
import com.zhilian.payment.dto.RefundDTO;
import com.zhilian.payment.service.PayService;
import com.zhilian.payment.vo.TradeCheckVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 支付控制器
 * 
 * @author 智联票务技术团队
 * @date 2026-04-01
 */
@RestController
@RequestMapping("/api/pay")
@Api(tags = "支付模块")
public class PayController {

    @Resource
    private PayService payService;

    /**
     * 通用支付
     * 
     * @param payDTO 支付信息
     * @return 支付链接
     */
    @PostMapping("/common")
    @ApiOperation("通用支付")
    public Result<String> commonPay(@RequestBody PayDTO payDTO) {
        return Result.success(payService.commonPay(payDTO));
    }

    /**
     * 交易查询
     * 
     * @param orderNumber 订单编号
     * @param channel 支付渠道
     * @return 交易查询结果
     */
    @GetMapping("/trade-check")
    @ApiOperation("交易查询")
    public Result<TradeCheckVO> tradeCheck(@RequestParam String orderNumber, @RequestParam String channel) {
        return Result.success(payService.tradeCheck(orderNumber, channel));
    }

    /**
     * 退款
     * 
     * @param refundDTO 退款信息
     * @return 退款结果
     */
    @PostMapping("/refund")
    @ApiOperation("退款")
    public Result<String> refund(@RequestBody RefundDTO refundDTO) {
        return Result.success(payService.refund(refundDTO));
    }

    /**
     * 查询支付结果
     * 
     * @param orderId 订单ID
     * @return 支付结果
     */
    @GetMapping("/result")
    @ApiOperation("查询支付结果")
    public Result getPayResult(@RequestParam Long orderId) {
        return Result.success(payService.getPayResult(orderId));
    }
}