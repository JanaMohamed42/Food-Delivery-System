package com.fooddelivery.entity;

import com.fooddelivery.enums.RestaurantCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String phone;

    private String email;

    // ── Address ───────────────────────────────────────────
    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    private String area;

    private Double latitude;
    private Double longitude;

    // ── Business Info ─────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantCategory category;

    @Column(name = "delivery_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "minimum_order", nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumOrder;

    @Column(name = "estimated_delivery_time", nullable = false)
    private Integer estimatedDeliveryTime; // in minutes

    // ── Rating ────────────────────────────────────────────
    @Column(nullable = false, precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "total_ratings", nullable = false)
    @Builder.Default
    private Integer totalRatings = 0;

    // ── Status ────────────────────────────────────────────
    @Column(name = "is_open", nullable = false)
    @Builder.Default
    private Boolean isOpen = false;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    private String imageUrl;

    // Relations
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private Set<MenuCategory> menuCategories = new LinkedHashSet<>();

    // Audit
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
