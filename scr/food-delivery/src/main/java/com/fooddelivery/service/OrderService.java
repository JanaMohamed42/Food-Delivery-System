package com.fooddelivery.service;

import com.fooddelivery.dto.request.CancelOrderRequest;
import com.fooddelivery.dto.request.CheckoutRequest;
import com.fooddelivery.dto.request.UpdateOrderStatusRequest;
import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.entity.Cart;
import com.fooddelivery.entity.Order;
import com.fooddelivery.entity.OrderItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.enums.OrderStatus;
import com.fooddelivery.exception.BusinessException;
import com.fooddelivery.exception.ResourceNotFoundException;
import com.fooddelivery.mapper.OrderMapper;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    //  Checkout

    @Transactional
    public OrderResponse checkout(String cartKey, CheckoutRequest request) {
        //  Validate cart وجيبها (بيعمل كل الـ validations)
        Cart cart = cartService.validateAndGetCart(cartKey);

        //  جيب الـ restaurant
        Restaurant restaurant = restaurantRepository.findByIdAndIsActiveTrue(cart.getRestaurantId())
                .orElseThrow(() -> new BusinessException(
                        "Restaurant not found", HttpStatus.BAD_REQUEST, "RESTAURANT_NOT_FOUND"));

        //   الـ order number
        String orderNumber = generateOrderNumber();

        // ابني الـ Order
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .userId(extractUserId(cartKey)) // nullable لحد ما اعمل User feature
                .restaurant(restaurant)
                .restaurantName(restaurant.getName()) // snapshot
                .deliveryStreet(request.deliveryStreet())
                .deliveryCity(request.deliveryCity())
                .deliveryArea(request.deliveryArea())
                .deliveryLatitude(request.deliveryLatitude())
                .deliveryLongitude(request.deliveryLongitude())
                .subtotal(cart.getSubtotal())
                .deliveryFee(cart.getDeliveryFee())
                .total(cart.getTotal())
                .status(OrderStatus.PENDING)
                .notes(request.notes())
                .build();

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .itemName(cartItem.getName())       // snapshot
                        .itemPrice(cartItem.getPrice())     // snapshot
                        .quantity(cartItem.getQuantity())
                        .subtotal(cartItem.getSubtotal())
                        .build())
                .toList();

        order.setItems(orderItems);

        //  حفظ في DB
        Order saved = orderRepository.save(order);

        //  مسح الـ cart من Redis
        cartService.clearCart(cartKey);

        log.info("Order created: {} for cart: {}", saved.getOrderNumber(), cartKey);
        return orderMapper.toResponse(saved);
    }

    //  Get Orders

    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(String cartKey) {
        UUID userId = extractUserId(cartKey);
        if (userId == null) {
            return List.of();
        }
        List<Order> orders = orderRepository.findAllByUserIdWithItems(userId);
        return orderMapper.toResponseList(orders);
    }

    //  Get Order by ID

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId));
        return orderMapper.toResponse(order);
    }

    //  Cancel Order
    @Transactional
    public OrderResponse cancelOrder(UUID orderId, CancelOrderRequest request) {
        Order order = findOrderById(orderId);

        if (!order.getStatus().isCancellable()) {
            throw new BusinessException(
                    String.format("Cannot cancel order in status: %s. Only PENDING or CONFIRMED orders can be cancelled.",
                            order.getStatus()),
                    HttpStatus.BAD_REQUEST,
                    "ORDER_NOT_CANCELLABLE"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(request != null ? request.reason() : null);

        Order saved = orderRepository.save(order);
        log.info("Order cancelled: {}", saved.getOrderNumber());
        return orderMapper.toResponse(saved);
    }

    //  Update Order Status (restaurant/admin)

    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, UpdateOrderStatusRequest request) {
        Order order = findOrderById(orderId);

        // validateTransition بيـ throw BusinessException لو الـ transition مش valid
        order.getStatus().validateTransition(request.status());

        order.setStatus(request.status());
        Order saved = orderRepository.save(order);

        log.info("Order {} status updated: {} → {}", saved.getOrderNumber(),
                order.getStatus(), request.status());
        return orderMapper.toResponse(saved);
    }

    //  Get Restaurant Orders
    @Transactional(readOnly = true)
    public List<OrderResponse> getRestaurantOrders(UUID restaurantId, OrderStatus status) {
        List<Order> orders;
        if (status != null) {
            orders = orderRepository.findAllByRestaurantIdAndStatusOrderByCreatedAtDesc(restaurantId, status);
        } else {
            orders = orderRepository.findAllByUserIdWithItems(restaurantId); // reuse
        }
        return orderMapper.toResponseList(orders);
    }

    //  Order Number Generation

    private String generateOrderNumber() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        long count = orderRepository.countOrdersToday(startOfDay, endOfDay);
        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String sequencePart = String.format("%04d", count + 1);

        return "ORD-" + datePart + "-" + sequencePart;
    }

    //  Helpers

    private Order findOrderById(UUID orderId) {
        return orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId));
    }

    //   userId من الـ cartKey
    // لو cartKey بدأ بـ UUID  هو الـ userId
    // لو لأ  guest session
    private UUID extractUserId(String cartKey) {
        try {
            return UUID.fromString(cartKey);
        } catch (IllegalArgumentException e) {
            return null; // guest
        }
    }
}
