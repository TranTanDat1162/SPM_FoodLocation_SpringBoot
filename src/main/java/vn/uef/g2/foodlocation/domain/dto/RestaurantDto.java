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
    private Double totalRating;
    private String description;
    private LocalTime openTime;
    private LocalTime closeTime;
}
