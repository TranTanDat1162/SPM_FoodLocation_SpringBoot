package vn.uef.g2.foodlocation.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "place_id")
    private String placeId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "create_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "restaurant",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    private List<Food> listFood = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RatingRestaurant> listRatingRestaurant = new ArrayList<>();

    @Column(name = "total_rating")
    private Double totalRating;

    @Column(name = "contact_no")
    private String phone;

    @Column(name = "parking_info")
    private String parkingInfo;

    public Restaurant() {
    }

    public Restaurant(String restaurantName,
                      String slug,
                      String description,
                      String address,
                      String latitude,
                      String longitude,
                      String image,
                      String phone,
                      String parkingInfo,
                      LocalTime openTime,
                      LocalTime closeTime,
                      boolean isActive,
                      Double totalRating
    ) {
        this.restaurantName = restaurantName;
        this.slug = slug;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.phone = phone;
        this.parkingInfo = parkingInfo;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isActive = isActive;
        this.totalRating = totalRating;
    }

    // add convenience methods for bi-directional relationship
    public void add(Food food) {
        if (listFood == null) {
            this.listFood = new ArrayList<>();
        }
        food.setRestaurant(this);
        this.listFood.add(food);
    }
}
