package com.fooddelivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart implements Serializable {

    // cart:{userId} or cart:{sessionId}
    private String cartKey;

    private UUID restaurantId;
    private String restaurantName;

    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    // ── Totals ────────────────────────────────────────────
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    private BigDecimal deliveryFee;

    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    // ── Nested CartItem ───────────────────────────────────
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItem implements Serializable {
        private UUID menuItemId;
        private String name;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal subtotal; // price × quantity
        private String imageUrl;
    }

    //  Helper Methods
    @JsonIgnore
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public boolean belongsToRestaurant(UUID restaurantId) {
        return this.restaurantId != null && this.restaurantId.equals(restaurantId);
    }

    public void recalculateTotals() {
        this.subtotal = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.total = this.subtotal.add(
                this.deliveryFee != null ? this.deliveryFee : BigDecimal.ZERO
        );
    }
}
