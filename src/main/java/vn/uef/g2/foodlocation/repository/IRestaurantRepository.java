package vn.uef.g2.foodlocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface IRestaurantRepository extends JpaRepository<Restaurant, Long> {

//    void save(Restaurant restaurant);
    Restaurant findRestaurantById(Long id);
    Optional<Restaurant> findByRestaurantName(String restaurantName);

    List<Restaurant> findByRestaurantNameContaining(String restaurantName);
    List<Restaurant> findExactByRestaurantName(String restaurantName);

    Restaurant findBySlug(String slug);

    @Query("SELECT r.slug FROM Restaurant r where r.id = :id")
    String findSlugById(@Param("id") Long restaurantId);

    //List<Restaurant> findAll();
    
    // find all restaurants by food name
//    @Query("SELECT r FROM Restaurant r LEFT JOIN Food f ON r.id = f.restaurant.id WHERE (f.foodName LIKE %:foodName% OR r.restaurantName LIKE %:foodName%)")
    @Query("SELECT r FROM Restaurant r LEFT JOIN r.listFood f WHERE f.foodName LIKE %:foodName% OR r.restaurantName LIKE %:foodName%")
    public Set<Restaurant> FindAllWithFoodNameQuery(@Param("foodName") String foodName);
}