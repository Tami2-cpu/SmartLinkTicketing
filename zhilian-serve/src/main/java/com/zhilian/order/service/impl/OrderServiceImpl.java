package com.zhilian.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhilian.core.exception.BusinessException;
import com.zhilian.order.entity.Order;
import com.zhilian.order.entity.OrderItem;
import com.zhilian.order.mapper.OrderItemMapper;
import com.zhilian.order.mapper.OrderMapper;
import com.zhilian.order.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

  @Resource
  private OrderMapper orderMapper;

  @Resource
  private OrderItemMapper orderItemMapper;

  @Override
  @Transactional
  public Order createOrder(Order order, List<OrderItem> items) {
    // 生成订单号
    String orderNo = generateOrderNo();
    order.setOrderNo(orderNo);
    order.setStatus(0); // 待支付
    order.setCreateTime(new Date());
    order.setUpdateTime(new Date());

    // 保存订单
    save(order);

    // 保存订单明细
    for (OrderItem item : items) {
      item.setOrderId(order.getId());
      item.setCreateTime(new Date());
      orderItemMapper.insert(item);
    }

    return order;
  }

  @Override
  public Order payOrder(Long orderId, Integer payType) {
    Order order = getById(orderId);
    if (order == null) {
      throw new BusinessException("订单不存在");
    }
    if (order.getStatus() != 0) {
      throw new BusinessException("订单状态不正确");
    }

    order.setStatus(1); // 已支付
    order.setPayType(payType);
    order.setPayTime(new Date());
    order.setUpdateTime(new Date());
    updateById(order);

    return order;
  }

  @Override
  public Order cancelOrder(Long orderId) {
    Order order = getById(orderId);
    if (order == null) {
      throw new BusinessException("订单不存在");
    }
    if (order.getStatus() != 0) {
      throw new BusinessException("订单状态不正确");
    }

    order.setStatus(2); // 已取消
    order.setUpdateTime(new Date());
    updateById(order);

    return order;
  }

  @Override
  public Order closeOrder(Long orderId) {
    Order order = getById(orderId);
    if (order == null) {
      throw new BusinessException("订单不存在");
    }

    order.setStatus(3); // 已关闭
    order.setUpdateTime(new Date());
    updateById(order);

    return order;
  }

  @Override
  public List<Order> getOrderList(Long userId) {
    return lambdaQuery().eq(Order::getUserId, userId).list();
  }

  @Override
  public Order getOrderById(Long orderId) {
    return getById(orderId);
  }

  @Override
  public List<OrderItem> getOrderItems(Long orderId) {
    QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("order_id", orderId);
    return orderItemMapper.selectList(queryWrapper);
  }

  private String generateOrderNo() {
    return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }
}