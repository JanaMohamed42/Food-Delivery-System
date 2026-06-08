package com.fooddelivery.mapper;

import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Order;
import com.fooddelivery.entity.OrderItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-07T16:15:53+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 26 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        UUID restaurantId = null;
        UUID id = null;
        String orderNumber = null;
        String restaurantName = null;
        String deliveryStreet = null;
        String deliveryCity = null;
        String deliveryArea = null;
        BigDecimal subtotal = null;
        BigDecimal deliveryFee = null;
        BigDecimal total = null;
        OrderStatus status = null;
        String notes = null;
        String cancellationReason = null;
        List<OrderResponse.OrderItemResponse> items = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        restaurantId = orderRestaurantId( order );
        id = order.getId();
        orderNumber = order.getOrderNumber();
        restaurantName = order.getRestaurantName();
        deliveryStreet = order.getDeliveryStreet();
        deliveryCity = order.getDeliveryCity();
        deliveryArea = order.getDeliveryArea();
        subtotal = order.getSubtotal();
        deliveryFee = order.getDeliveryFee();
        total = order.getTotal();
        status = order.getStatus();
        notes = order.getNotes();
        cancellationReason = order.getCancellationReason();
        items = orderItemListToOrderItemResponseList( order.getItems() );
        createdAt = order.getCreatedAt();
        updatedAt = order.getUpdatedAt();

        OrderResponse orderResponse = new OrderResponse( id, orderNumber, restaurantId, restaurantName, deliveryStreet, deliveryCity, deliveryArea, subtotal, deliveryFee, total, status, notes, cancellationReason, items, createdAt, updatedAt );

        return orderResponse;
    }

    @Override
    public OrderResponse.OrderItemResponse toItemResponse(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        UUID menuItemId = null;
        UUID id = null;
        String itemName = null;
        BigDecimal itemPrice = null;
        Integer quantity = null;
        BigDecimal subtotal = null;

        menuItemId = itemMenuItemId( item );
        id = item.getId();
        itemName = item.getItemName();
        itemPrice = item.getItemPrice();
        quantity = item.getQuantity();
        subtotal = item.getSubtotal();

        OrderResponse.OrderItemResponse orderItemResponse = new OrderResponse.OrderItemResponse( id, menuItemId, itemName, itemPrice, quantity, subtotal );

        return orderItemResponse;
    }

    @Override
    public List<OrderResponse> toResponseList(List<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        List<OrderResponse> list = new ArrayList<OrderResponse>( orders.size() );
        for ( Order order : orders ) {
            list.add( toResponse( order ) );
        }

        return list;
    }

    private UUID orderRestaurantId(Order order) {
        if ( order == null ) {
            return null;
        }
        Restaurant restaurant = order.getRestaurant();
        if ( restaurant == null ) {
            return null;
        }
        UUID id = restaurant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<OrderResponse.OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderResponse.OrderItemResponse> list1 = new ArrayList<OrderResponse.OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toItemResponse( orderItem ) );
        }

        return list1;
    }

    private UUID itemMenuItemId(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }
        MenuItem menuItem = orderItem.getMenuItem();
        if ( menuItem == null ) {
            return null;
        }
        UUID id = menuItem.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
