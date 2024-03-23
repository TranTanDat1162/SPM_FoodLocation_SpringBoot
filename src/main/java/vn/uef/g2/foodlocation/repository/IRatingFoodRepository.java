package vn.uef.g2.foodlocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.uef.g2.foodlocation.domain.entity.RatingFood;
import vn.uef.g2.foodlocation.domain.entity.RatingRestaurant;

import java.util.List;

@Repository
public interface IRatingFoodRepository extends JpaRepository<RatingFood, Long> {
    List<RatingFood> findByFoodId(Long foodId);
    //    AVG rating
    @Query("SELECT AVG(rf.rateStar) FROM RatingFood rf WHERE rf.food.id = :foodId")
    Double calculateAverageRatingByFoodId(@Param("foodId") Long foodId);

    //count rating
    @Query("SELECT COUNT(rf) FROM RatingFood rf WHERE rf.food.id = :foodId")
    long countByFoodId(@Param("foodId") Long foodId);
}
