package com.fooddelivery.mapper;

import com.fooddelivery.dto.response.CartResponse;
import com.fooddelivery.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalItems", expression = "java(cart.getItems().stream().mapToInt(com.fooddelivery.entity.Cart.CartItem::getQuantity).sum())")
    CartResponse toResponse(Cart cart);

    CartResponse.CartItemResponse toItemResponse(Cart.CartItem item);
}
