package com.fooddelivery.dto.response;

import com.fooddelivery.enums.RestaurantCategory;

import java.math.BigDecimal;
import java.util.UUID;

public record RestaurantSummaryResponse(
        UUID id,
        String name,
        String description,
        RestaurantCategory category,
        String imageUrl,
        BigDecimal rating,
        Integer totalRatings,
        BigDecimal deliveryFee,
        BigDecimal minimumOrder,
        Integer estimatedDeliveryTime,
        Boolean isOpen,
        String city,
        String area
) {}
