package com.zhilian.payment.service;

import com.zhilian.order.entity.Order;
import com.zhilian.payment.dto.NotifyDTO;
import com.zhilian.payment.dto.PayDTO;
import com.zhilian.payment.dto.RefundDTO;
import com.zhilian.payment.vo.NotifyVO;
import com.zhilian.payment.vo.TradeCheckVO;

public interface PayService {

    String commonPay(PayDTO payDTO);

    NotifyVO notify(NotifyDTO notifyDTO);

    TradeCheckVO tradeCheck(String orderNumber, String channel);

    String refund(RefundDTO refundDTO);

    Order getPayResult(Long orderId);
}