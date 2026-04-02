package com.zhilian.order.controller;

import com.zhilian.core.exception.Result;
import com.zhilian.core.utils.UserActionLogger;
import com.zhilian.order.entity.Order;
import com.zhilian.order.entity.OrderItem;
import com.zhilian.order.service.OrderService;
import com.zhilian.order.vo.OrderDetailVO;
import com.zhilian.order.vo.OrderListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 * 
 * @author 智联票务技术团队
 * @date 2026-04-01
 */
@RestController
@RequestMapping("/api/order")
@Api(tags = "订单模块")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 创建订单
     * 
     * @param orderInfo 订单信息
     * @param request   请求对象
     * @return 创建的订单
     */
    @PostMapping("/create")
    @ApiOperation("创建订单")
    public Result<Order> createOrder(@RequestBody Map<String, Object> orderInfo, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String username = UserActionLogger.getCurrentUsername();
        try {
            Order order = new Order();
            order.setUserId(1L); // 模拟用户ID
            order.setEventId((Long) orderInfo.get("eventId"));
            order.setTotalAmount((java.math.BigDecimal) orderInfo.get("totalAmount"));

            List<OrderItem> items = (List<OrderItem>) orderInfo.get("items");
            Order createdOrder = orderService.createOrder(order, items);

            UserActionLogger.logOrderCreate(username, createdOrder.getOrderNo(), true, ip);
            return Result.success(createdOrder);
        } catch (Exception e) {
            UserActionLogger.logOrderCreate(username, "", false, ip);
            throw e;
        }
    }

    /**
     * 订单支付
     * 
     * @param payInfo 支付信息
     * @param request 请求对象
     * @return 支付后的订单
     */
    @PostMapping("/pay")
    @ApiOperation("订单支付")
    public Result<Order> payOrder(@RequestBody Map<String, Object> payInfo, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String username = UserActionLogger.getCurrentUsername();
        Long orderId = (Long) payInfo.get("orderId");
        try {
            Integer payType = (Integer) payInfo.get("payType");
            Order paidOrder = orderService.payOrder(orderId, payType);
            UserActionLogger.logOrderPay(username, paidOrder.getOrderNo(), true, ip);
            return Result.success(paidOrder);
        } catch (Exception e) {
            UserActionLogger.logOrderPay(username, "", false, ip);
            throw e;
        }
    }

    /**
     * 取消订单
     * 
     * @param cancelInfo 取消信息
     * @param request    请求对象
     * @return 取消结果
     */
    @PostMapping("/cancel")
    @ApiOperation("取消订单")
    public Result<String> cancelOrder(@RequestBody Map<String, Long> cancelInfo, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String username = UserActionLogger.getCurrentUsername();
        Long orderId = cancelInfo.get("orderId");
        try {
            orderService.cancelOrder(orderId);
            UserActionLogger.logOrderCancel(username, "", true, ip);
            return Result.success("取消成功");
        } catch (Exception e) {
            UserActionLogger.logOrderCancel(username, "", false, ip);
            throw e;
        }
    }

    /**
     * 获取订单列表
     * 
     * @return 订单列表
     */
    @GetMapping("/list")
    @ApiOperation("获取订单列表")
    public Result<List<OrderListVO>> getOrderList() {
        // 实际项目中需要从上下文获取用户ID
        Long userId = 1L; // 模拟用户ID
        List<OrderListVO> orders = orderService.getOrderList(userId);
        return Result.success(orders);
    }

    /**
     * 获取订单详情
     * 
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/detail")
    @ApiOperation("获取订单详情")
    public Result<OrderDetailVO> getOrderDetail(@RequestParam Long orderId) {
        OrderDetailVO orderDetail = orderService.getOrderDetail(orderId);
        return Result.success(orderDetail);
    }

    /**
     * 关闭订单
     * 
     * @param closeInfo 关闭信息
     * @return 关闭结果
     */
    @PostMapping("/close")
    @ApiOperation("关闭订单")
    public Result<String> closeOrder(@RequestBody Map<String, Long> closeInfo) {
        Long orderId = closeInfo.get("orderId");
        orderService.closeOrder(orderId);
        return Result.success("关闭成功");
    }

    /**
     * 初始化取消订单
     * 
     * @param orderId 订单ID
     * @return 取消结果
     */
    @PostMapping("/initiate-cancel/{orderId}")
    @ApiOperation("初始化取消订单")
    public Result<Boolean> initiateCancel(@PathVariable Long orderId) {
        boolean result = orderService.initiateCancel(orderId);
        return Result.success(result);
    }
}