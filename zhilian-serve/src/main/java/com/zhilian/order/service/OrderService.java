package com.zhilian.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhilian.order.entity.Order;
import com.zhilian.order.entity.OrderItem;

import java.util.List;

public interface OrderService extends IService<Order> {
    Order createOrder(Order order, List<OrderItem> items);
    Order payOrder(Long orderId, Integer payType);
    Order cancelOrder(Long orderId);
    Order closeOrder(Long orderId);
    List<Order> getOrderList(Long userId);
    Order getOrderById(Long orderId);
    List<OrderItem> getOrderItems(Long orderId);
}