package com.fooddelivery.controller;

import com.fooddelivery.dto.request.CreateMenuCategoryRequest;
import com.fooddelivery.dto.request.CreateMenuItemRequest;
import com.fooddelivery.dto.request.CreateRestaurantRequest;
import com.fooddelivery.dto.response.MenuItemResponse;
import com.fooddelivery.dto.response.RestaurantDetailResponse;
import com.fooddelivery.dto.response.RestaurantSummaryResponse;
import com.fooddelivery.enums.RestaurantCategory;
import com.fooddelivery.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantSummaryResponse>> getAllRestaurants(
            @RequestParam(required = false) Boolean openOnly,
            @RequestParam(required = false) RestaurantCategory category) {
        return ResponseEntity.ok(restaurantService.getAllRestaurants(openOnly, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponse> getRestaurant(@PathVariable UUID id) {
        return ResponseEntity.ok(restaurantService.getRestaurantWithMenu(id));
    }

    @PostMapping
    public ResponseEntity<RestaurantSummaryResponse> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.createRestaurant(request));
    }

    @PatchMapping("/{id}/toggle-open")
    public ResponseEntity<RestaurantSummaryResponse> toggleOpen(@PathVariable UUID id) {
        return ResponseEntity.ok(restaurantService.toggleOpen(id));
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<RestaurantDetailResponse> addCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CreateMenuCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.addMenuCategory(id, request));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<MenuItemResponse> addMenuItem(
            @PathVariable UUID id,
            @Valid @RequestBody CreateMenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restaurantService.addMenuItem(id, request));
    }

    @PatchMapping("/{restaurantId}/items/{itemId}/toggle-availability")
    public ResponseEntity<MenuItemResponse> toggleItemAvailability(
            @PathVariable UUID restaurantId,
            @PathVariable UUID itemId) {
        return ResponseEntity.ok(restaurantService.toggleItemAvailability(restaurantId, itemId));
    }
}
