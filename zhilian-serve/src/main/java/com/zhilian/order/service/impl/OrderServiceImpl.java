package com.zhilian.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhilian.core.exception.BusinessException;
import com.zhilian.order.entity.Order;
import com.zhilian.order.entity.OrderItem;
import com.zhilian.order.mapper.OrderItemMapper;
import com.zhilian.order.mapper.OrderMapper;
import com.zhilian.order.service.OrderService;
import com.zhilian.order.vo.OrderDetailVO;
import com.zhilian.order.vo.OrderItemVO;
import com.zhilian.order.vo.OrderListVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现
 * 
 * @author 智联票务技术团队
 * @date 2026-04-01
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

  @Resource
  private OrderMapper orderMapper;

  @Resource
  private OrderItemMapper orderItemMapper;

  @Resource
  private RedissonClient redissonClient;

  @Override
  @Transactional(rollbackFor = Exception.class)
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
  @Transactional(rollbackFor = Exception.class)
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

    // 更新订单相关数据
    updateOrderRelatedData(order.getOrderNo(), order.getStatus());

    return order;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Order cancelOrder(Long orderId) {
    Order order = getById(orderId);
    if (order == null) {
      throw new BusinessException("订单不存在");
    }
    if (order.getStatus() != 0) {
      throw new BusinessException("订单状态不正确");
    }

    order.setStatus(2); // 已取消
    order.setCancelTime(new Date());
    order.setUpdateTime(new Date());
    updateById(order);

    // 更新订单相关数据
    updateOrderRelatedData(order.getOrderNo(), order.getStatus());

    return order;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Order closeOrder(Long orderId) {
    Order order = getById(orderId);
    if (order == null) {
      throw new BusinessException("订单不存在");
    }

    order.setStatus(3); // 已关闭
    order.setCloseTime(new Date());
    order.setUpdateTime(new Date());
    updateById(order);

    // 更新订单相关数据
    updateOrderRelatedData(order.getOrderNo(), order.getStatus());

    return order;
  }

  @Override
  public List<OrderListVO> getOrderList(Long userId) {
    List<Order> orders = lambdaQuery().eq(Order::getUserId, userId).list();
    List<OrderListVO> orderListVOs = new ArrayList<>();
    for (Order order : orders) {
      OrderListVO orderListVO = new OrderListVO();
      BeanUtils.copyProperties(order, orderListVO);
      // 计算订单中的票数
      int ticketCount = 0;
      List<OrderItem> orderItems = getOrderItems(order.getId());
      ticketCount = orderItems.size();
      orderListVO.setTicketCount(ticketCount);
      orderListVOs.add(orderListVO);
    }
    return orderListVOs;
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

  @Override
  public OrderDetailVO getOrderDetail(Long orderId) {
    Order order = getById(orderId);
    if (order == null) {
      return null;
    }
    OrderDetailVO orderDetailVO = new OrderDetailVO();
    BeanUtils.copyProperties(order, orderDetailVO);
    // 转换订单明细
    List<OrderItem> orderItems = getOrderItems(orderId);
    List<OrderItemVO> orderItemVOs = new ArrayList<>();
    for (OrderItem item : orderItems) {
      OrderItemVO orderItemVO = new OrderItemVO();
      BeanUtils.copyProperties(item, orderItemVO);
      orderItemVOs.add(orderItemVO);
    }
    orderDetailVO.setOrderItems(orderItemVOs);
    return orderDetailVO;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean initiateCancel(Long orderId) {
    // 分布式锁
    RLock lock = redissonClient.getLock("order:cancel:" + orderId);
    try {
      if (lock.tryLock()) {
        Order order = getById(orderId);
        if (order != null && order.getStatus() == 0) {
          order.setStatus(2); // 已取消
          order.setCancelTime(new Date());
          order.setUpdateTime(new Date());
          updateById(order);
          // 更新订单相关数据
          updateOrderRelatedData(order.getOrderNo(), order.getStatus());
          return true;
        }
      }
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
    return false;
  }

  @Override
  public void updateOrderRelatedData(String orderNumber, Integer orderStatus) {
    // 这里可以实现订单状态变更后的相关操作，例如：
    // 1. 更新缓存中的订单状态
    // 2. 发送订单状态变更通知
    // 3. 处理订单相关的业务逻辑
    // 暂时为空实现
  }

  private String generateOrderNo() {
    return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }
}