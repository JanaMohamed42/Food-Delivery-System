package com.fooddelivery.mapper;

import com.fooddelivery.dto.response.OrderResponse;
import com.fooddelivery.entity.Order;
import com.fooddelivery.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "restaurantId", source = "restaurant.id")
    OrderResponse toResponse(Order order);

    @Mapping(target = "menuItemId", source = "menuItem.id")
    OrderResponse.OrderItemResponse toItemResponse(OrderItem item);

    List<OrderResponse> toResponseList(List<Order> orders);
}
