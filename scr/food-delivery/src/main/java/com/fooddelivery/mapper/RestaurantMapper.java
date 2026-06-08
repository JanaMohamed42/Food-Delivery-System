package com.fooddelivery.mapper;

import com.fooddelivery.dto.request.CreateMenuItemRequest;
import com.fooddelivery.dto.request.CreateRestaurantRequest;
import com.fooddelivery.dto.response.MenuItemResponse;
import com.fooddelivery.dto.response.RestaurantDetailResponse;
import com.fooddelivery.dto.response.RestaurantSummaryResponse;
import com.fooddelivery.entity.MenuCategory;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Restaurant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantSummaryResponse toSummaryResponse(Restaurant restaurant);

    @Mapping(target = "menu", source = "menuCategories")
    RestaurantDetailResponse toDetailResponse(Restaurant restaurant);

    @Mapping(target = "items", source = "items")
    RestaurantDetailResponse.MenuCategoryResponse toCategoryResponse(MenuCategory category);

    MenuItemResponse toMenuItemResponse(MenuItem menuItem);

    List<MenuItemResponse> toMenuItemResponseList(List<MenuItem> items);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isOpen", constant = "false")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "rating", constant = "0.0")
    @Mapping(target = "totalRatings", constant = "0")
    @Mapping(target = "menuCategories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Restaurant toEntity(CreateRestaurantRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "displayOrder", source = "displayOrder", defaultValue = "0")
    MenuCategory toCategoryEntity(com.fooddelivery.dto.request.CreateMenuCategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "isAvailable", constant = "true")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "displayOrder", source = "displayOrder", defaultValue = "0")
    MenuItem toMenuItemEntity(CreateMenuItemRequest request);
}
