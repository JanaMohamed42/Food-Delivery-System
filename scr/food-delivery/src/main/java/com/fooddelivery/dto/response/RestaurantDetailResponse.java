package com.fooddelivery.dto.response;

import com.fooddelivery.enums.RestaurantCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RestaurantDetailResponse(
        UUID id,
        String name,
        String description,
        String phone,
        String email,
        RestaurantCategory category,
        String imageUrl,
        BigDecimal rating,
        Integer totalRatings,
        BigDecimal deliveryFee,
        BigDecimal minimumOrder,
        Integer estimatedDeliveryTime,
        Boolean isOpen,
        String street,
        String city,
        String area,
        Double latitude,
        Double longitude,
        List<MenuCategoryResponse> menu
) {
    public record MenuCategoryResponse(
            UUID id,
            String name,
            Integer displayOrder,
            List<MenuItemResponse> items
    ) {}
}
