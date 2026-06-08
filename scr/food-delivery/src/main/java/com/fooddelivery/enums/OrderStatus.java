package com.fooddelivery.enums;

import com.fooddelivery.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.Map;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    // Valid transitions map
    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = Map.of(
            PENDING,          Set.of(CONFIRMED, CANCELLED),
            CONFIRMED,        Set.of(PREPARING, CANCELLED),
            PREPARING,        Set.of(OUT_FOR_DELIVERY),
            OUT_FOR_DELIVERY, Set.of(DELIVERED),
            DELIVERED,        Set.of(),
            CANCELLED,        Set.of()
    );

    public void validateTransition(OrderStatus next) {
        if (!VALID_TRANSITIONS.get(this).contains(next)) {
            throw new BusinessException(
                    String.format("Cannot transition order from %s to %s", this.name(), next.name()),
                    HttpStatus.BAD_REQUEST,
                    "INVALID_ORDER_TRANSITION"
            );
        }
    }

    public boolean isCancellable() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELLED;
    }
}
