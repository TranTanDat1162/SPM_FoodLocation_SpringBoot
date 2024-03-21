package vn.uef.g2.foodlocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.uef.g2.foodlocation.domain.entity.Category;

public interface ICategoryRepository extends JpaRepository<Category, Long> {

}
