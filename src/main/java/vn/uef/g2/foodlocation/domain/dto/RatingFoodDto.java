package vn.uef.g2.foodlocation.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingFoodDto {
    private Long id;
    private String content;
    private int rateStar;
    private LocalDateTime createdAt;
    private String foodName;
    private String userFullName;
    private String userAvatar;
}
