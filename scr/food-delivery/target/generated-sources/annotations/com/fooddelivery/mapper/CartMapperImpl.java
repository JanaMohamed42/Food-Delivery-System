package com.fooddelivery.mapper;

import com.fooddelivery.dto.response.CartResponse;
import com.fooddelivery.entity.Cart;
import java.math.BigDecimal;
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
public class CartMapperImpl implements CartMapper {

    @Override
    public CartResponse toResponse(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        UUID restaurantId = null;
        String restaurantName = null;
        List<CartResponse.CartItemResponse> items = null;
        BigDecimal subtotal = null;
        BigDecimal deliveryFee = null;
        BigDecimal total = null;

        restaurantId = cart.getRestaurantId();
        restaurantName = cart.getRestaurantName();
        items = cartItemListToCartItemResponseList( cart.getItems() );
        subtotal = cart.getSubtotal();
        deliveryFee = cart.getDeliveryFee();
        total = cart.getTotal();

        int totalItems = cart.getItems().stream().mapToInt(com.fooddelivery.entity.Cart.CartItem::getQuantity).sum();

        CartResponse cartResponse = new CartResponse( restaurantId, restaurantName, items, subtotal, deliveryFee, total, totalItems );

        return cartResponse;
    }

    @Override
    public CartResponse.CartItemResponse toItemResponse(Cart.CartItem item) {
        if ( item == null ) {
            return null;
        }

        UUID menuItemId = null;
        String name = null;
        BigDecimal price = null;
        Integer quantity = null;
        BigDecimal subtotal = null;
        String imageUrl = null;

        menuItemId = item.getMenuItemId();
        name = item.getName();
        price = item.getPrice();
        quantity = item.getQuantity();
        subtotal = item.getSubtotal();
        imageUrl = item.getImageUrl();

        CartResponse.CartItemResponse cartItemResponse = new CartResponse.CartItemResponse( menuItemId, name, price, quantity, subtotal, imageUrl );

        return cartItemResponse;
    }

    protected List<CartResponse.CartItemResponse> cartItemListToCartItemResponseList(List<Cart.CartItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CartResponse.CartItemResponse> list1 = new ArrayList<CartResponse.CartItemResponse>( list.size() );
        for ( Cart.CartItem cartItem : list ) {
            list1.add( toItemResponse( cartItem ) );
        }

        return list1;
    }
}
