package com.zhilian.order.controller;

import com.zhilian.core.exception.Result;
import com.zhilian.core.utils.UserActionLogger;
import com.zhilian.order.entity.Order;
import com.zhilian.order.entity.OrderItem;
import com.zhilian.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@Api(tags = "订单模块")
public class OrderController {

    @Resource
    private OrderService orderService;

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

    @GetMapping("/list")
    @ApiOperation("获取订单列表")
    public Result<List<Order>> getOrderList() {
        // 实际项目中需要从上下文获取用户ID
        Long userId = 1L; // 模拟用户ID
        List<Order> orders = orderService.getOrderList(userId);
        return Result.success(orders);
    }

    @GetMapping("/detail")
    @ApiOperation("获取订单详情")
    public Result<Map<String, Object>> getOrderDetail(@RequestParam Long orderId) {
        Order order = orderService.getOrderById(orderId);
        List<OrderItem> items = orderService.getOrderItems(orderId);
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return Result.success(result);
    }

    @PostMapping("/close")
    @ApiOperation("关闭订单")
    public Result<String> closeOrder(@RequestBody Map<String, Long> closeInfo) {
        Long orderId = closeInfo.get("orderId");
        orderService.closeOrder(orderId);
        return Result.success("关闭成功");
    }
}