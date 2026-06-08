package com.fooddelivery.dto.response;

import com.fooddelivery.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String orderNumber,
        UUID restaurantId,
        String restaurantName,
        String deliveryStreet,
        String deliveryCity,
        String deliveryArea,
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal total,
        OrderStatus status,
        String notes,
        String cancellationReason,
        List<OrderItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record OrderItemResponse(
            UUID id,
            UUID menuItemId,
            String itemName,
            BigDecimal itemPrice,
            Integer quantity,
            BigDecimal subtotal
    ) {}
}
