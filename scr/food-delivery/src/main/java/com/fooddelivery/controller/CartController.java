package com.fooddelivery.controller;

import com.fooddelivery.dto.request.AddToCartRequest;
import com.fooddelivery.dto.request.UpdateCartItemRequest;
import com.fooddelivery.dto.response.CartResponse;
import com.fooddelivery.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // دلوقتي بستخدم X-Cart-Key header كـ cartKey
    // لما  اعمل User feature هيتغير لـ userId من الـ JWT token
    // لو مفيش header  session ID تلقائي بشكل مؤقت

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader(value = "X-Cart-Key", required = false, defaultValue = "guest") String cartKey) {
        return ResponseEntity.ok(cartService.getCart(cartKey));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @RequestHeader(value = "X-Cart-Key", required = false, defaultValue = "guest") String cartKey,
            @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addItem(cartKey, request));
    }

    @PutMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponse> updateItem(
            @RequestHeader(value = "X-Cart-Key", required = false, defaultValue = "guest") String cartKey,
            @PathVariable UUID menuItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateItem(cartKey, menuItemId, request));
    }

    @DeleteMapping("/items/{menuItemId}")
    public ResponseEntity<CartResponse> removeItem(
            @RequestHeader(value = "X-Cart-Key", required = false, defaultValue = "guest") String cartKey,
            @PathVariable UUID menuItemId) {
        return ResponseEntity.ok(cartService.removeItem(cartKey, menuItemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestHeader(value = "X-Cart-Key", required = false, defaultValue = "guest") String cartKey) {
        cartService.clearCart(cartKey);
        return ResponseEntity.noContent().build();
    }

    // لما يكون في conflict وال user يوافق على clear
    @PostMapping("/clear-and-add")
    public ResponseEntity<CartResponse> clearAndAdd(
            @RequestHeader(value = "X-Cart-Key", required = false, defaultValue = "guest") String cartKey,
            @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.clearAndAdd(cartKey, request));
    }
}
