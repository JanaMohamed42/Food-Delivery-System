package com.fooddelivery.service;

import com.fooddelivery.dto.request.CreateMenuCategoryRequest;
import com.fooddelivery.dto.request.CreateMenuItemRequest;
import com.fooddelivery.dto.request.CreateRestaurantRequest;
import com.fooddelivery.dto.response.MenuItemResponse;
import com.fooddelivery.dto.response.RestaurantDetailResponse;
import com.fooddelivery.dto.response.RestaurantSummaryResponse;
import com.fooddelivery.entity.MenuCategory;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.enums.RestaurantCategory;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.mapper.RestaurantMapper;
import com.fooddelivery.repository.MenuCategoryRepository;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantMapper restaurantMapper;

    //  Browse Restaurants

    @Transactional(readOnly = true)
    public List<RestaurantSummaryResponse> getAllRestaurants(Boolean openOnly, RestaurantCategory category) {
        List<Restaurant> restaurants;

        if (category != null && openOnly != null && openOnly) {
            restaurants = restaurantRepository.findAllByIsActiveTrueAndIsOpenTrueAndCategoryOrderByRatingDesc(category);
        } else if (category != null) {
            restaurants = restaurantRepository.findAllByIsActiveTrueAndCategoryOrderByRatingDesc(category);
        } else if (openOnly != null && openOnly) {
            restaurants = restaurantRepository.findAllByIsActiveTrueAndIsOpenTrueOrderByRatingDesc();
        } else {
            restaurants = restaurantRepository.findAllByIsActiveTrueOrderByRatingDesc();
        }

        return restaurants.stream()
                .map(restaurantMapper::toSummaryResponse)
                .toList();
    }

    //  Restaurant Details + Full Menu

    @Transactional(readOnly = true)
    public RestaurantDetailResponse getRestaurantWithMenu(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdWithActiveMenu(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id: " + restaurantId));

        return restaurantMapper.toDetailResponse(restaurant);
    }

    //  Create Restaurant

    @Transactional
    public RestaurantSummaryResponse createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Created restaurant: {} with id: {}", saved.getName(), saved.getId());
        return restaurantMapper.toSummaryResponse(saved);
    }

    //  Toggle Open/Closed

    @Transactional
    public RestaurantSummaryResponse toggleOpen(UUID restaurantId) {
        Restaurant restaurant = findActiveRestaurantById(restaurantId);
        restaurant.setIsOpen(!restaurant.getIsOpen());
        Restaurant saved = restaurantRepository.save(restaurant);
        log.info("Restaurant {} is now {}", saved.getName(), saved.getIsOpen() ? "OPEN" : "CLOSED");
        return restaurantMapper.toSummaryResponse(saved);
    }

    //  Menu Category

    @Transactional
    public RestaurantDetailResponse addMenuCategory(UUID restaurantId, CreateMenuCategoryRequest request) {
        Restaurant restaurant = findActiveRestaurantById(restaurantId);

        MenuCategory category = restaurantMapper.toCategoryEntity(request);
        category.setRestaurant(restaurant);

        menuCategoryRepository.save(category);
        log.info("Added category '{}' to restaurant: {}", category.getName(), restaurant.getName());

        // reload with full menu
        return getRestaurantWithMenu(restaurantId);
    }

    // Menu Item

    @Transactional
    public MenuItemResponse addMenuItem(UUID restaurantId, CreateMenuItemRequest request) {
        Restaurant restaurant = findActiveRestaurantById(restaurantId);

        MenuCategory category = menuCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu category not found with id: " + request.categoryId()));

        // validate category belongs to this restaurant
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException(
                    "Category does not belong to restaurant: " + restaurantId);
        }

        MenuItem item = restaurantMapper.toMenuItemEntity(request);
        item.setCategory(category);
        item.setRestaurant(restaurant);

        MenuItem saved = menuItemRepository.save(item);
        log.info("Added menu item '{}' to restaurant: {}", saved.getName(), restaurant.getName());

        return restaurantMapper.toMenuItemResponse(saved);
    }

    //  Toggle Item Availability

    @Transactional
    public MenuItemResponse toggleItemAvailability(UUID restaurantId, UUID itemId) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item not found with id: " + itemId));

        if (!item.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Item does not belong to this restaurant");
        }

        item.setIsAvailable(!item.getIsAvailable());
        MenuItem saved = menuItemRepository.save(item);
        log.info("Menu item '{}' is now {}", saved.getName(), saved.getIsAvailable() ? "AVAILABLE" : "UNAVAILABLE");

        return restaurantMapper.toMenuItemResponse(saved);
    }

    //  Private Helpers

    private Restaurant findActiveRestaurantById(UUID restaurantId) {
        return restaurantRepository.findByIdAndIsActiveTrue(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Restaurant not found with id: " + restaurantId));
    }
}
