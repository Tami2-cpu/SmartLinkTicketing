package com.zhilian.payment.service;

import com.zhilian.order.entity.Order;

public interface PayService {
    String createWxPay(Long orderId, Double amount);
    String createAlipay(Long orderId, Double amount);
    boolean handleWxCallback(String callbackData);
    boolean handleAlipayCallback(String callbackData);
    Order getPayResult(Long orderId);
}