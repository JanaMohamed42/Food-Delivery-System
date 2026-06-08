package com.fooddelivery.service;

import com.fooddelivery.dto.request.AddToCartRequest;
import com.fooddelivery.dto.request.UpdateCartItemRequest;
import com.fooddelivery.dto.response.CartResponse;
import com.fooddelivery.entity.Cart;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.exception.BusinessException;
import com.fooddelivery.exception.CartConflictException;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.mapper.CartMapper;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final CartMapper cartMapper;

    @Value("${app.cart.ttl-seconds:604800}")
    private long cartTtlSeconds;

    private static final String CART_KEY_PREFIX = "cart:";

    //  Get Cart

    public CartResponse getCart(String cartKey) {
        Cart cart = getCartFromRedis(cartKey);
        if (cart == null || cart.isEmpty()) {
            return emptyCartResponse();
        }
        return cartMapper.toResponse(cart);
    }

    //  Add Item

    public CartResponse addItem(String cartKey, AddToCartRequest request) {
        // menuItem  إنه متاح
        MenuItem menuItem = menuItemRepository
                .findByIdAndIsActiveTrueAndIsAvailableTrue(request.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Menu item not found or unavailable: " + request.menuItemId()));

        Restaurant restaurant = menuItem.getRestaurant();

        //  cart الحالية من Redis
        Cart cart = getCartFromRedis(cartKey);

        if (cart == null || cart.isEmpty()) {
            //  Cart فاضية
            cart = buildNewCart(cartKey, restaurant);
        } else {
            //  Cart فيها items - يتأكد إنهم من نفس المطعم
            if (!cart.belongsToRestaurant(restaurant.getId())) {
                throw new CartConflictException(
                        cart.getRestaurantId(),
                        cart.getRestaurantName()
                );
            }
        }

        //  item موجود في الـ cart؟
        Optional<Cart.CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getMenuItemId().equals(menuItem.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // زود الـ quantity
            Cart.CartItem item = existingItem.get();
            int newQty = item.getQuantity() + request.quantity();
            if (newQty > 20) {
                throw new BusinessException(
                        "Cannot add more than 20 of the same item",
                        HttpStatus.BAD_REQUEST,
                        "MAX_QUANTITY_EXCEEDED"
                );
            }
            item.setQuantity(newQty);
            item.setSubtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(newQty)));
        } else {
            // ضيف item جديد
            Cart.CartItem newItem = Cart.CartItem.builder()
                    .menuItemId(menuItem.getId())
                    .name(menuItem.getName())
                    .price(menuItem.getPrice())
                    .quantity(request.quantity())
                    .subtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(request.quantity())))
                    .imageUrl(menuItem.getImageUrl())
                    .build();
            cart.getItems().add(newItem);
        }

        //  احسب الـ totals وحفظ
        cart.recalculateTotals();
        saveCartToRedis(cartKey, cart);

        log.info("Added item '{}' to cart: {}", menuItem.getName(), cartKey);
        return cartMapper.toResponse(cart);
    }

    //  Update Item Quantity
    public CartResponse updateItem(String cartKey, UUID menuItemId, UpdateCartItemRequest request) {
        Cart cart = getCartOrThrow(cartKey);

        Cart.CartItem item = cart.getItems().stream()
                .filter(i -> i.getMenuItemId().equals(menuItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item not found in cart: " + menuItemId));

        if (request.quantity() == 0) {
            // quantity = 0 → احذف الـ item
            cart.getItems().remove(item);
        } else {
            item.setQuantity(request.quantity());
            item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(request.quantity())));
        }

        // لو  فضيت تتشال من Redis
        if (cart.getItems().isEmpty()) {
            clearCart(cartKey);
            return emptyCartResponse();
        }

        cart.recalculateTotals();
        saveCartToRedis(cartKey, cart);

        return cartMapper.toResponse(cart);
    }

    //  Remove Item

    public CartResponse removeItem(String cartKey, UUID menuItemId) {
        return updateItem(cartKey, menuItemId, new UpdateCartItemRequest(0));
    }

    //  Clear Cart
    public void clearCart(String cartKey) {
        redisTemplate.delete(buildRedisKey(cartKey));
        log.info("Cleared cart: {}", cartKey);
    }

    //  Clear and Replace ( conflict resolution)

    public CartResponse clearAndAdd(String cartKey, AddToCartRequest request) {
        clearCart(cartKey);
        return addItem(cartKey, request);
    }

    //  Validate Cart before Checkout
    public Cart validateAndGetCart(String cartKey) {
        Cart cart = getCartFromRedis(cartKey);

        if (cart == null || cart.isEmpty()) {
            throw new BusinessException("Cart is empty", HttpStatus.BAD_REQUEST, "CART_EMPTY");
        }

        // المطعم لسه open
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(cart.getRestaurantId())
                .orElseThrow(() -> new BusinessException(
                        "Restaurant is no longer available",
                        HttpStatus.BAD_REQUEST,
                        "RESTAURANT_UNAVAILABLE"
                ));

        if (!restaurant.getIsOpen()) {
            throw new BusinessException(
                    "Restaurant is currently closed",
                    HttpStatus.BAD_REQUEST,
                    "RESTAURANT_CLOSED"
            );
        }

        // items لسه available
        List<UUID> itemIds = cart.getItems().stream()
                .map(Cart.CartItem::getMenuItemId)
                .toList();

        List<MenuItem> availableItems = menuItemRepository.findAllByIdInAndIsActiveTrueAndIsAvailableTrue(itemIds);

        if (availableItems.size() != itemIds.size()) {
            List<UUID> availableIds = availableItems.stream().map(MenuItem::getId).toList();
            List<String> unavailableNames = cart.getItems().stream()
                    .filter(i -> !availableIds.contains(i.getMenuItemId()))
                    .map(Cart.CartItem::getName)
                    .toList();

            throw new BusinessException(
                    "Some items are no longer available: " + String.join(", ", unavailableNames),
                    HttpStatus.BAD_REQUEST,
                    "ITEMS_UNAVAILABLE"
            );
        }

        //  total >= minimum order
        if (cart.getSubtotal().compareTo(restaurant.getMinimumOrder()) < 0) {
            throw new BusinessException(
                    String.format("Minimum order is %.2f, current subtotal is %.2f",
                            restaurant.getMinimumOrder(), cart.getSubtotal()),
                    HttpStatus.BAD_REQUEST,
                    "BELOW_MINIMUM_ORDER"
            );
        }

        return cart;
    }

    //  Redis Helpers

    public Cart getCartFromRedis(String cartKey) {
        try {
            Object value = redisTemplate.opsForValue().get(buildRedisKey(cartKey));
            if (value instanceof Cart cart) {
                return cart;
            }
            return null;
        } catch (Exception e) {
            log.error("Error reading cart from Redis for key: {}", cartKey, e);
            return null;
        }
    }

    private void saveCartToRedis(String cartKey, Cart cart) {
        redisTemplate.opsForValue().set(
                buildRedisKey(cartKey),
                cart,
                cartTtlSeconds,
                TimeUnit.SECONDS
        );
    }

    private Cart getCartOrThrow(String cartKey) {
        Cart cart = getCartFromRedis(cartKey);
        if (cart == null || cart.isEmpty()) {
            throw new BusinessException("Cart is empty", HttpStatus.BAD_REQUEST, "CART_EMPTY");
        }
        return cart;
    }

    private Cart buildNewCart(String cartKey, Restaurant restaurant) {
        return Cart.builder()
                .cartKey(cartKey)
                .restaurantId(restaurant.getId())
                .restaurantName(restaurant.getName())
                .deliveryFee(restaurant.getDeliveryFee())
                .items(new ArrayList<>())
                .build();
    }

    private String buildRedisKey(String cartKey) {
        return CART_KEY_PREFIX + cartKey;
    }

    private CartResponse emptyCartResponse() {
        return new CartResponse(null, null, List.of(),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0);
    }
}
