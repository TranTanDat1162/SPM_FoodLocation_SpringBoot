package vn.uef.g2.foodlocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;

import java.util.List;

@Repository
public interface IFoodRepository extends JpaRepository<Food, Long> {
    Food findBySlug(String slug);
    List<Food> findFoodByRestaurantId(Long restaurantId);
    Food findFoodById(Long foodId);
    @Query("SELECT f.slug FROM Food f where f.id = :id")
    String findSlugById(@Param("id") Long foodId);
}
