package com.zhilian.order.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单明细请求DTO
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public class OrderItemDTO {
    
    @NotNull(message = "票种ID不能为空")
    private Long ticketId;
    
    @NotNull(message = "数量不能为空")
    private Integer quantity;
    
    @NotNull(message = "单价不能为空")
    private BigDecimal price;
    
    private Long buyerId;

    // getters and setters
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }
}