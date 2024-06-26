package vn.uef.g2.foodlocation.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @Column(name = "image")
    private String image;

    @Column(name = "time_wait")
    private int timeWait;

    @Column(name = "is_recommended")
    private Boolean isRecommended;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "create_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Food() {
    }

    public Food(String foodName, String slug, String description, int price, String image, int timeWait, boolean isRecommended) {
        this.foodName = foodName;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.image = image;
        this.timeWait = timeWait;
        this.isRecommended = isRecommended;
    }
}
