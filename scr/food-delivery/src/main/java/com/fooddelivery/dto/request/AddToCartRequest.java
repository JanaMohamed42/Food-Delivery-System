package com.fooddelivery.dto.request;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record AddToCartRequest(
        @NotNull(message = "Menu item ID is required")
        UUID menuItemId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 20, message = "Quantity cannot exceed 20")
        Integer quantity
) {}
