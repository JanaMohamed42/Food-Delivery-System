package com.fooddelivery.dto.request;

import jakarta.validation.constraints.*;

public record CreateMenuCategoryRequest(
        @NotBlank(message = "Name is required")
        String name,

        Integer displayOrder
) {}
