package com.fooddelivery.dto.request;

import com.fooddelivery.enums.RestaurantCategory;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateRestaurantRequest(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotBlank(message = "Phone is required")
        String phone,

        String email,

        @NotBlank(message = "Street is required")
        String street,

        @NotBlank(message = "City is required")
        String city,

        String area,

        Double latitude,
        Double longitude,

        @NotNull(message = "Category is required")
        RestaurantCategory category,

        @NotNull(message = "Delivery fee is required")
        @DecimalMin(value = "0.0", message = "Delivery fee cannot be negative")
        BigDecimal deliveryFee,

        @NotNull(message = "Minimum order is required")
        @DecimalMin(value = "0.0", message = "Minimum order cannot be negative")
        BigDecimal minimumOrder,

        @NotNull(message = "Estimated delivery time is required")
        @Min(value = 1, message = "Estimated delivery time must be at least 1 minute")
        Integer estimatedDeliveryTime,

        String imageUrl
) {}
