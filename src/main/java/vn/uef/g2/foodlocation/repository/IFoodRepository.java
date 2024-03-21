package vn.uef.g2.foodlocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;

public interface IFoodRepository extends JpaRepository<Food, Long> {
    Food findBySlug(String slug);
}
