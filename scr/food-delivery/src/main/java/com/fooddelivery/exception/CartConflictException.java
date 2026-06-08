package com.fooddelivery.exception;

import lombok.Getter;
import java.util.UUID;

@Getter
public class CartConflictException extends RuntimeException {

    private final UUID existingRestaurantId;
    private final String existingRestaurantName;

    public CartConflictException(UUID existingRestaurantId, String existingRestaurantName) {
        super("Cart contains items from a different restaurant: " + existingRestaurantName);
        this.existingRestaurantId = existingRestaurantId;
        this.existingRestaurantName = existingRestaurantName;
    }
}
