package com.zhilian.order.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;import java.util.List;

/**
 * 订单请求DTO
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public class OrderDTO {
    
    @NotNull(message = "活动ID不能为空")
    private Long eventId;
    
    @NotNull(message = "总金额不能为空")
    private BigDecimal totalAmount;
    
    @NotNull(message = "支付方式不能为空")
    private Integer payType;
    
    private List<OrderItemDTO> items;

    // getters and setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}