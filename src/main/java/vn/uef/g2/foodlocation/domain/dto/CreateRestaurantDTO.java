package vn.uef.g2.foodlocation.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
@Data
public class CreateRestaurantDTO {
    private String placeId;
    @NotNull
    private String restaurantName;
    @NotNull
    private String address;
    private Double totalRating;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
}
