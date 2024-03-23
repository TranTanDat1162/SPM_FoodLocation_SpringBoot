package vn.uef.g2.foodlocation.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingRestaurantDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt ;
    private String restaurantName;
    private String userFullName;
    private String userAvatar;
}
