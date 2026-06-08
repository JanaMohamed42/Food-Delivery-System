package com.fooddelivery.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID restaurantId,
        String restaurantName,
        List<CartItemResponse> items,
        BigDecimal subtotal,
        BigDecimal deliveryFee,
        BigDecimal total,
        int totalItems
) {
    public record CartItemResponse(
            UUID menuItemId,
            String name,
            BigDecimal price,
            Integer quantity,
            BigDecimal subtotal,
            String imageUrl
    ) {}
}
