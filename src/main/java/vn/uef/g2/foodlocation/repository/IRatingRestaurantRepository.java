package vn.uef.g2.foodlocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.uef.g2.foodlocation.domain.entity.RatingRestaurant;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRatingRestaurantRepository extends JpaRepository<RatingRestaurant, Long> {
    List<RatingRestaurant> findByRestaurantId(Long restaurantId);

    //    find by restaurantId & userId
    Optional<RatingRestaurant> findByRestaurantIdAndUserId(Long restaurantId, Long UserId);

    //count rating
    @Query ("select count(rr) from RatingRestaurant rr where rr.restaurant.id = :restaurantId")
    long countByRestaurantId(@Param("restaurantId") Long restaurantId);
}
