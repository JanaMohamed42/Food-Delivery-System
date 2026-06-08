package com.fooddelivery.mapper;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-07T16:15:53+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 26 (Oracle Corporation)"
)
@Component
public class RestaurantMapperImpl implements RestaurantMapper {

    @Override
    public RestaurantSummaryResponse toSummaryResponse(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        RestaurantCategory category = null;
        String imageUrl = null;
        BigDecimal rating = null;
        Integer totalRatings = null;
        BigDecimal deliveryFee = null;
        BigDecimal minimumOrder = null;
        Integer estimatedDeliveryTime = null;
        Boolean isOpen = null;
        String city = null;
        String area = null;

        id = restaurant.getId();
        name = restaurant.getName();
        description = restaurant.getDescription();
        category = restaurant.getCategory();
        imageUrl = restaurant.getImageUrl();
        rating = restaurant.getRating();
        totalRatings = restaurant.getTotalRatings();
        deliveryFee = restaurant.getDeliveryFee();
        minimumOrder = restaurant.getMinimumOrder();
        estimatedDeliveryTime = restaurant.getEstimatedDeliveryTime();
        isOpen = restaurant.getIsOpen();
        city = restaurant.getCity();
        area = restaurant.getArea();

        RestaurantSummaryResponse restaurantSummaryResponse = new RestaurantSummaryResponse( id, name, description, category, imageUrl, rating, totalRatings, deliveryFee, minimumOrder, estimatedDeliveryTime, isOpen, city, area );

        return restaurantSummaryResponse;
    }

    @Override
    public RestaurantDetailResponse toDetailResponse(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }

        List<RestaurantDetailResponse.MenuCategoryResponse> menu = null;
        UUID id = null;
        String name = null;
        String description = null;
        String phone = null;
        String email = null;
        RestaurantCategory category = null;
        String imageUrl = null;
        BigDecimal rating = null;
        Integer totalRatings = null;
        BigDecimal deliveryFee = null;
        BigDecimal minimumOrder = null;
        Integer estimatedDeliveryTime = null;
        Boolean isOpen = null;
        String street = null;
        String city = null;
        String area = null;
        Double latitude = null;
        Double longitude = null;

        menu = menuCategorySetToMenuCategoryResponseList( restaurant.getMenuCategories() );
        id = restaurant.getId();
        name = restaurant.getName();
        description = restaurant.getDescription();
        phone = restaurant.getPhone();
        email = restaurant.getEmail();
        category = restaurant.getCategory();
        imageUrl = restaurant.getImageUrl();
        rating = restaurant.getRating();
        totalRatings = restaurant.getTotalRatings();
        deliveryFee = restaurant.getDeliveryFee();
        minimumOrder = restaurant.getMinimumOrder();
        estimatedDeliveryTime = restaurant.getEstimatedDeliveryTime();
        isOpen = restaurant.getIsOpen();
        street = restaurant.getStreet();
        city = restaurant.getCity();
        area = restaurant.getArea();
        latitude = restaurant.getLatitude();
        longitude = restaurant.getLongitude();

        RestaurantDetailResponse restaurantDetailResponse = new RestaurantDetailResponse( id, name, description, phone, email, category, imageUrl, rating, totalRatings, deliveryFee, minimumOrder, estimatedDeliveryTime, isOpen, street, city, area, latitude, longitude, menu );

        return restaurantDetailResponse;
    }

    @Override
    public RestaurantDetailResponse.MenuCategoryResponse toCategoryResponse(MenuCategory category) {
        if ( category == null ) {
            return null;
        }

        List<MenuItemResponse> items = null;
        UUID id = null;
        String name = null;
        Integer displayOrder = null;

        items = toMenuItemResponseList( category.getItems() );
        id = category.getId();
        name = category.getName();
        displayOrder = category.getDisplayOrder();

        RestaurantDetailResponse.MenuCategoryResponse menuCategoryResponse = new RestaurantDetailResponse.MenuCategoryResponse( id, name, displayOrder, items );

        return menuCategoryResponse;
    }

    @Override
    public MenuItemResponse toMenuItemResponse(MenuItem menuItem) {
        if ( menuItem == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        String description = null;
        BigDecimal price = null;
        String imageUrl = null;
        Boolean isAvailable = null;
        Integer displayOrder = null;

        id = menuItem.getId();
        name = menuItem.getName();
        description = menuItem.getDescription();
        price = menuItem.getPrice();
        imageUrl = menuItem.getImageUrl();
        isAvailable = menuItem.getIsAvailable();
        displayOrder = menuItem.getDisplayOrder();

        MenuItemResponse menuItemResponse = new MenuItemResponse( id, name, description, price, imageUrl, isAvailable, displayOrder );

        return menuItemResponse;
    }

    @Override
    public List<MenuItemResponse> toMenuItemResponseList(List<MenuItem> items) {
        if ( items == null ) {
            return null;
        }

        List<MenuItemResponse> list = new ArrayList<MenuItemResponse>( items.size() );
        for ( MenuItem menuItem : items ) {
            list.add( toMenuItemResponse( menuItem ) );
        }

        return list;
    }

    @Override
    public Restaurant toEntity(CreateRestaurantRequest request) {
        if ( request == null ) {
            return null;
        }

        Restaurant.RestaurantBuilder restaurant = Restaurant.builder();

        restaurant.name( request.name() );
        restaurant.description( request.description() );
        restaurant.phone( request.phone() );
        restaurant.email( request.email() );
        restaurant.street( request.street() );
        restaurant.city( request.city() );
        restaurant.area( request.area() );
        restaurant.latitude( request.latitude() );
        restaurant.longitude( request.longitude() );
        restaurant.category( request.category() );
        restaurant.deliveryFee( request.deliveryFee() );
        restaurant.minimumOrder( request.minimumOrder() );
        restaurant.estimatedDeliveryTime( request.estimatedDeliveryTime() );
        restaurant.imageUrl( request.imageUrl() );

        restaurant.isOpen( false );
        restaurant.isActive( true );
        restaurant.rating( new BigDecimal( "0.0" ) );
        restaurant.totalRatings( 0 );

        return restaurant.build();
    }

    @Override
    public MenuCategory toCategoryEntity(CreateMenuCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        MenuCategory.MenuCategoryBuilder menuCategory = MenuCategory.builder();

        if ( request.displayOrder() != null ) {
            menuCategory.displayOrder( request.displayOrder() );
        }
        else {
            menuCategory.displayOrder( 0 );
        }
        menuCategory.name( request.name() );

        menuCategory.isActive( true );

        return menuCategory.build();
    }

    @Override
    public MenuItem toMenuItemEntity(CreateMenuItemRequest request) {
        if ( request == null ) {
            return null;
        }

        MenuItem.MenuItemBuilder menuItem = MenuItem.builder();

        if ( request.displayOrder() != null ) {
            menuItem.displayOrder( request.displayOrder() );
        }
        else {
            menuItem.displayOrder( 0 );
        }
        menuItem.name( request.name() );
        menuItem.description( request.description() );
        menuItem.price( request.price() );
        menuItem.imageUrl( request.imageUrl() );

        menuItem.isAvailable( true );
        menuItem.isActive( true );

        return menuItem.build();
    }

    protected List<RestaurantDetailResponse.MenuCategoryResponse> menuCategorySetToMenuCategoryResponseList(Set<MenuCategory> set) {
        if ( set == null ) {
            return null;
        }

        List<RestaurantDetailResponse.MenuCategoryResponse> list = new ArrayList<RestaurantDetailResponse.MenuCategoryResponse>( set.size() );
        for ( MenuCategory menuCategory : set ) {
            list.add( toCategoryResponse( menuCategory ) );
        }

        return list;
    }
}
