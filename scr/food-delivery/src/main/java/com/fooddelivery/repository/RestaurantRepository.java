package com.fooddelivery.repository;

import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.enums.RestaurantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    // كل المطاعم الشغاله
    List<Restaurant> findAllByIsActiveTrueOrderByRatingDesc();

    // المطاعم المفتوحة
    List<Restaurant> findAllByIsActiveTrueAndIsOpenTrueOrderByRatingDesc();

    // فلتر بالـ category
    List<Restaurant> findAllByIsActiveTrueAndCategoryOrderByRatingDesc(RestaurantCategory category);

    // فلتر بالـ category + open
    List<Restaurant> findAllByIsActiveTrueAndIsOpenTrueAndCategoryOrderByRatingDesc(RestaurantCategory category);

    // Restaurant بالـ ID مع الـ menuCategories و items (بـ JOIN FETCH عشان اتجنب N+1)
    @Query("""
            SELECT DISTINCT r FROM Restaurant r
            LEFT JOIN FETCH r.menuCategories mc
            LEFT JOIN FETCH mc.items i
            WHERE r.id = :id
              AND r.isActive = true
              AND (mc.isActive = true OR mc IS NULL)
              AND (i.isActive = true AND i.isAvailable = true OR i IS NULL)
            """)
    Optional<Restaurant> findByIdWithActiveMenu(UUID id);

    Optional<Restaurant> findByIdAndIsActiveTrue(UUID id);
}
