package com.fooddelivery.repository;

import com.fooddelivery.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    //  item  بالـ ID
    Optional<MenuItem> findByIdAndIsActiveTrueAndIsAvailableTrue(UUID id);

    //  items المتاحة
    List<MenuItem> findAllByRestaurantIdAndIsActiveTrueAndIsAvailableTrue(UUID restaurantId);

    // للـ validation قبل checkout - نشوف الـ items لسه متاحة
    List<MenuItem> findAllByIdInAndIsActiveTrueAndIsAvailableTrue(List<UUID> ids);
}
