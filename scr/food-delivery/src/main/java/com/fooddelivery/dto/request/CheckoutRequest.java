package com.fooddelivery.dto.request;

import jakarta.validation.constraints.*;

public record CheckoutRequest(
        @NotBlank(message = "Street address is required")
        String deliveryStreet,

        @NotBlank(message = "City is required")
        String deliveryCity,

        String deliveryArea,

        Double deliveryLatitude,
        Double deliveryLongitude,

        String notes
) {}
