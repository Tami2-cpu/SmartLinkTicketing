package com.zhilian.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhilian.order.entity.Order;
import com.zhilian.order.entity.OrderItem;
import com.zhilian.order.vo.OrderDetailVO;
import com.zhilian.order.vo.OrderListVO;

import java.util.List;

/**
 * 订单服务接口
 * 
 * @author 智联票务技术团队
 * @date 2026-04-01
 */
public interface OrderService extends IService<Order> {

  /**
   * 创建订单
   * 
   * @param order 订单信息
   * @param items 订单明细
   * @return 创建的订单
   */
  Order createOrder(Order order, List<OrderItem> items);

  /**
   * 支付订单
   * 
   * @param orderId 订单ID
   * @param payType 支付类型
   * @return 支付后的订单
   */
  Order payOrder(Long orderId, Integer payType);

  /**
   * 取消订单
   * 
   * @param orderId 订单ID
   * @return 取消后的订单
   */
  Order cancelOrder(Long orderId);

  /**
   * 关闭订单
   * 
   * @param orderId 订单ID
   * @return 关闭后的订单
   */
  Order closeOrder(Long orderId);

  /**
   * 获取用户订单列表
   * 
   * @param userId 用户ID
   * @return 订单列表
   */
  List<OrderListVO> getOrderList(Long userId);

  /**
   * 根据ID获取订单
   * 
   * @param orderId 订单ID
   * @return 订单信息
   */
  Order getOrderById(Long orderId);

  /**
   * 获取订单明细
   * 
   * @param orderId 订单ID
   * @return 订单明细列表
   */
  List<OrderItem> getOrderItems(Long orderId);

  /**
   * 获取订单详情
   * 
   * @param orderId 订单ID
   * @return 订单详情
   */
  OrderDetailVO getOrderDetail(Long orderId);

  /**
   * 初始化取消订单
   * 
   * @param orderId 订单ID
   * @return 是否成功
   */
  boolean initiateCancel(Long orderId);

  /**
   * 更新订单相关数据
   * 
   * @param orderNumber 订单编号
   * @param orderStatus 订单状态
   */
  void updateOrderRelatedData(String orderNumber, Integer orderStatus);
}