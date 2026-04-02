package com.zhilian.payment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhilian.core.exception.BusinessException;
import com.zhilian.core.lock.LockType;
import com.zhilian.core.lock.ServiceLock;
import com.zhilian.order.entity.Order;
import com.zhilian.order.service.OrderService;
import com.zhilian.payment.dto.NotifyDTO;
import com.zhilian.payment.dto.PayDTO;
import com.zhilian.payment.dto.RefundDTO;
import com.zhilian.payment.entity.PayBill;
import com.zhilian.payment.entity.RefundBill;
import com.zhilian.payment.mapper.PayBillMapper;
import com.zhilian.payment.mapper.RefundBillMapper;
import com.zhilian.payment.service.PayService;
import com.zhilian.payment.strategy.PayResult;
import com.zhilian.payment.strategy.PayStrategyContext;
import com.zhilian.payment.strategy.PayStrategyHandler;
import com.zhilian.payment.strategy.RefundResult;
import com.zhilian.payment.strategy.TradeResult;
import com.zhilian.payment.vo.NotifyVO;
import com.zhilian.payment.vo.TradeCheckVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 支付服务实现类
 * 
 * @author 智联票务技术团队
 * @date 2026-04-01
 */
@Service
public class PayServiceImpl implements PayService {

    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Resource
    private OrderService orderService;

    @Resource
    private PayBillMapper payBillMapper;

    @Resource
    private RefundBillMapper refundBillMapper;

    @Resource
    private PayStrategyContext payStrategyContext;

    private static final String ALIPAY_NOTIFY_SUCCESS_RESULT = "success";
    private static final String ALIPAY_NOTIFY_FAILURE_RESULT = "failure";
    private static final String COMMON_PAY = "common_pay";
    private static final String TRADE_CHECK = "trade_check";

    @Override
    @ServiceLock(name = COMMON_PAY, keys = {"#payDTO.orderNumber"})
    @Transactional(rollbackFor = Exception.class)
    public String commonPay(PayDTO payDTO) {
        LambdaQueryWrapper<PayBill> payBillLambdaQueryWrapper = 
                Wrappers.lambdaQuery(PayBill.class).eq(PayBill::getOutOrderNo, payDTO.getOrderNumber());
        PayBill payBill = payBillMapper.selectOne(payBillLambdaQueryWrapper);
        if (Objects.nonNull(payBill) && payBill.getPayBillStatus() != 0) {
            throw new BusinessException("支付账单状态不正确");
        }
        PayStrategyHandler payStrategyHandler = payStrategyContext.get(payDTO.getChannel());
        PayResult pay = payStrategyHandler.pay(payDTO.getOrderNumber(), payDTO.getPrice(), 
                payDTO.getSubject(), payDTO.getNotifyUrl(), payDTO.getReturnUrl());
        if (pay.isSuccess()) {
            if (Objects.isNull(payBill)){
                payBill = new PayBill();
                payBill.setOutOrderNo(payDTO.getOrderNumber());
                payBill.setPayChannel(payDTO.getChannel());
                payBill.setPayScene("生产");
                payBill.setPayAmount(payDTO.getPrice());
                payBill.setPayBillStatus(0); // 0表示未支付
                payBill.setPayTime(new Date());
                payBill.setCreateTime(new Date());
                payBill.setUpdateTime(new Date());
                payBillMapper.insert(payBill);
            }else {
                PayBill updatePayBill = new PayBill();
                updatePayBill.setId(payBill.getId());
                updatePayBill.setPayTime(new Date());
                updatePayBill.setUpdateTime(new Date());
                payBillMapper.updateById(updatePayBill);
            }
        }
        return pay.getBody();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NotifyVO notify(NotifyDTO notifyDTO) {
        NotifyVO notifyVO = new NotifyVO();
        logger.info("回调通知参数 ===> {}", notifyDTO.getParams());
        Map<String, String> params = notifyDTO.getParams();

        PayStrategyHandler payStrategyHandler = payStrategyContext.get(notifyDTO.getChannel());
        boolean signVerifyResult = payStrategyHandler.signVerify(params);
        if (!signVerifyResult) {
            notifyVO.setPayResult(ALIPAY_NOTIFY_FAILURE_RESULT);
            return notifyVO;
        }
        LambdaQueryWrapper<PayBill> payBillLambdaQueryWrapper = 
                Wrappers.lambdaQuery(PayBill.class).eq(PayBill::getOutOrderNo, params.get("out_trade_no"));
        PayBill payBill = payBillMapper.selectOne(payBillLambdaQueryWrapper);
        if (Objects.isNull(payBill)) {
            logger.error("账单为空 notifyDTO : {}", notifyDTO);
            notifyVO.setPayResult(ALIPAY_NOTIFY_FAILURE_RESULT);
            return notifyVO;
        }
        if (payBill.getPayBillStatus() == 1) {
            logger.info("账单已支付 notifyDTO : {}", notifyDTO);
            notifyVO.setOutTradeNo(payBill.getOutOrderNo());
            notifyVO.setPayResult(ALIPAY_NOTIFY_SUCCESS_RESULT);
            return notifyVO;
        }
        boolean dataVerify = payStrategyHandler.dataVerify(notifyDTO.getParams(), payBill);
        if (!dataVerify) {
            notifyVO.setPayResult(ALIPAY_NOTIFY_FAILURE_RESULT);
            return notifyVO;
        }
        PayBill updatePayBill = new PayBill();
        updatePayBill.setPayBillStatus(1); // 1表示已支付
        updatePayBill.setUpdateTime(new Date());
        LambdaUpdateWrapper<PayBill> payBillLambdaUpdateWrapper = 
                Wrappers.lambdaUpdate(PayBill.class).eq(PayBill::getOutOrderNo, params.get("out_trade_no"));
        payBillMapper.update(updatePayBill, payBillLambdaUpdateWrapper);
        notifyVO.setOutTradeNo(payBill.getOutOrderNo());
        notifyVO.setPayResult(ALIPAY_NOTIFY_SUCCESS_RESULT);
        return notifyVO;
    }

    @Override
    @ServiceLock(name = TRADE_CHECK, keys = {"#orderNumber"})
    public TradeCheckVO tradeCheck(String orderNumber, String channel) {
        TradeCheckVO tradeCheckVO = new TradeCheckVO();
        PayStrategyHandler payStrategyHandler = payStrategyContext.get(channel);
        TradeResult tradeResult = payStrategyHandler.queryTrade(orderNumber);
        BeanUtil.copyProperties(tradeResult, tradeCheckVO);
        if (!tradeResult.isSuccess()) {
            return tradeCheckVO;
        }
        BigDecimal totalAmount = tradeResult.getTotalAmount();
        String outTradeNo = tradeResult.getOutTradeNo();
        Integer payBillStatus = tradeResult.getPayBillStatus();
        LambdaQueryWrapper<PayBill> payBillLambdaQueryWrapper = 
                Wrappers.lambdaQuery(PayBill.class).eq(PayBill::getOutOrderNo, outTradeNo);
        PayBill payBill = payBillMapper.selectOne(payBillLambdaQueryWrapper);
        if (Objects.isNull(payBill)) {
            logger.error("账单为空 orderNumber : {}", orderNumber);
            return tradeCheckVO;
        }
        if (payBill.getPayAmount().compareTo(totalAmount) != 0) {
            logger.error("支付渠道 和库中账单支付金额不一致 支付渠道支付金额 : {}, 库中账单支付金额 : {}, orderNumber : {}",
                    totalAmount, payBill.getPayAmount(), orderNumber);
            return tradeCheckVO;
        }
        if (!Objects.equals(payBill.getPayBillStatus(), payBillStatus)) {
            logger.warn("支付渠道和库中账单交易状态不一致 支付渠道payBillStatus : {}, 库中payBillStatus : {}, orderNumber : {}",
                    payBillStatus, payBill.getPayBillStatus(), orderNumber);
            PayBill updatePayBill = new PayBill();
            updatePayBill.setId(payBill.getId());
            updatePayBill.setPayBillStatus(payBillStatus);
            updatePayBill.setUpdateTime(new Date());
            LambdaUpdateWrapper<PayBill> payBillLambdaUpdateWrapper = 
                    Wrappers.lambdaUpdate(PayBill.class).eq(PayBill::getOutOrderNo, outTradeNo);
            payBillMapper.update(updatePayBill, payBillLambdaUpdateWrapper);
            return tradeCheckVO;
        }
        return tradeCheckVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String refund(RefundDTO refundDTO) {
        PayBill payBill = payBillMapper.selectOne(Wrappers.lambdaQuery(PayBill.class)
                .eq(PayBill::getOutOrderNo, refundDTO.getOrderNumber()));
        if (Objects.isNull(payBill)) {
            throw new BusinessException("支付账单不存在");
        }

        if (payBill.getPayBillStatus() != 1) {
            throw new BusinessException("支付账单状态不正确");
        }

        if (refundDTO.getAmount().compareTo(payBill.getPayAmount()) > 0) {
            throw new BusinessException("退款金额大于支付金额");
        }

        PayStrategyHandler payStrategyHandler = payStrategyContext.get(refundDTO.getChannel());
        RefundResult refundResult = 
                payStrategyHandler.refund(refundDTO.getOrderNumber(), refundDTO.getAmount(), refundDTO.getReason());
        if (refundResult.isSuccess()) {
            PayBill updatePayBill = new PayBill();
            updatePayBill.setId(payBill.getId());
            updatePayBill.setPayBillStatus(2); // 2表示已退款
            updatePayBill.setUpdateTime(new Date());
            payBillMapper.updateById(updatePayBill);
            RefundBill refundBill = new RefundBill();
            refundBill.setOutOrderNo(payBill.getOutOrderNo());
            refundBill.setPayBillId(payBill.getId());
            refundBill.setRefundAmount(refundDTO.getAmount());
            refundBill.setRefundStatus(1); // 1表示已退款
            refundBill.setRefundTime(new Date());
            refundBill.setReason(refundDTO.getReason());
            refundBill.setCreateTime(new Date());
            refundBill.setUpdateTime(new Date());
            refundBillMapper.insert(refundBill);
            return refundBill.getOutOrderNo();
        } else {
            throw new BusinessException(refundResult.getMessage());
        }
    }

    @Override
    public Order getPayResult(Long orderId) {
        // 获取订单支付结果
        return orderService.getOrderById(orderId);
    }
}