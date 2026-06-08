package com.fooddelivery.dto.request;

import jakarta.validation.constraints.*;

public record UpdateCartItemRequest(
        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity cannot be negative")
        @Max(value = 20, message = "Quantity cannot exceed 20")
        Integer quantity
) {}
