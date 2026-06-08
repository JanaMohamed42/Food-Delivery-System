package com.fooddelivery.repository;

import com.fooddelivery.entity.Order;
import com.fooddelivery.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    // Orders بتاعت يوزر معين مرتبة بالأحدث
    List<Order> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    // Order بالـ ID مع الـ items (بدون N+1)
    @Query("""
            SELECT DISTINCT o FROM Order o
            LEFT JOIN FETCH o.items oi
            LEFT JOIN FETCH o.restaurant r
            WHERE o.id = :id
            """)
    Optional<Order> findByIdWithItems(UUID id);

    // Orders بتاعت يوزر مع الـ items (للـ list view)
    @Query("""
            SELECT DISTINCT o FROM Order o
            LEFT JOIN FETCH o.items
            WHERE o.userId = :userId
            ORDER BY o.createdAt DESC
            """)
    List<Order> findAllByUserIdWithItems(UUID userId);

    // Count orders of today للـ order number generation
    @Query("""
            SELECT COUNT(o) FROM Order o
            WHERE o.createdAt >= :startOfDay
              AND o.createdAt < :endOfDay
            """)
    long countOrdersToday(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Order> findByOrderNumber(String orderNumber);

    // Orders بـ status معين للمطعم
    List<Order> findAllByRestaurantIdAndStatusOrderByCreatedAtDesc(UUID restaurantId, OrderStatus status);
}
