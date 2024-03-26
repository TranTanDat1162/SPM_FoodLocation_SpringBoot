package vn.uef.g2.foodlocation.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;

@Data
public class RestaurantDto {
    private Long id;
    private String placeId;
    @NotNull
    private String restaurantName;
    @NotNull
    private String address;
    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
    private String phone;
    private String parking;
    private Double totalRating;
    private String description;
    private String openTime;
    private String closeTime;
}
