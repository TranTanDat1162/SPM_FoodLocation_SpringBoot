package vn.uef.g2.foodlocation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.uef.g2.foodlocation.domain.dto.RatingFoodDto;
import vn.uef.g2.foodlocation.domain.dto.RatingRestaurantDto;
import vn.uef.g2.foodlocation.domain.dto.RestaurantDto;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.FoodService;
import vn.uef.g2.foodlocation.service.RatingFoodService;
import vn.uef.g2.foodlocation.service.RatingRestaurantService;
import vn.uef.g2.foodlocation.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final RestaurantService restaurantService;
    private final FoodService foodService;
    private final RatingRestaurantService ratingRestaurantService;
    private final RatingFoodService ratingFoodService;

    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        List<Restaurant> restaurants = restaurantService.findRestaurants();
        List<Food> foodList = foodService.findAll();
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("foods", foodList);
        List<RatingRestaurantDto> ratingList= ratingRestaurantService.findAll();
        model.addAttribute("ratings", ratingList);
        return "client/index";
    }

    @GetMapping("/restaurant/{slug}")
    public String getRestaurantDetail(Model model, @PathVariable("slug") String slug) {

        Optional<Restaurant> restaurant = restaurantService.findBySlug(slug);
        List<Food> foodListByRestaurant = foodService.findListFoodByRestaurantId(restaurant.get().getId());

        List<RatingRestaurantDto> ratingList = ratingRestaurantService.getRatingsByRestaurantId(restaurant.get().getId());

        model.addAttribute("restaurantId", restaurant.get().getId());
        model.addAttribute("ratingList", ratingList);

        model.addAttribute("restaurant", restaurant.get());
        model.addAttribute("foodList", foodListByRestaurant);
        return "client/restaurantDetail";
    }
    @GetMapping("/food/{slug}")
    public String getFoodDetail(Model model, @PathVariable("slug") String slug) {
        Optional<Food> food = foodService.findBySlug(slug);
        List<Food> foodList = foodService.findAll();

        List<RatingFoodDto> ratingList = ratingFoodService.getRatingsByFoodId(food.get().getId());
        Double ratingValue = ratingFoodService.calculateAverageRatingByFoodId(food.get().getId());
        long countRating=ratingFoodService.countRating(food.get().getId());

        model.addAttribute("foodId", food.get().getId());
        model.addAttribute("foods", foodList);
        model.addAttribute("ratingList", ratingList);
        model.addAttribute("ratingValue", ratingValue);
        if(ratingValue == null) {
            model.addAttribute("ratingValue", 0);
        }
        model.addAttribute("countRating", countRating);
        model.addAttribute("food", food.get());
        return "client/foodDetail";
    }
    @GetMapping(value = {"/search"})
    public String getKeyword(@RequestParam String keyword,String district, String radius, Model model) {

        List<Restaurant> restaurantsByName = restaurantService.findMatchingRestaurant(keyword);
        Set<Restaurant> restaurantsByFood = restaurantService.findAllRestaurantsWithFoodName(keyword);

        // Merge 2 results
        List<Restaurant> totalRestaurants = new ArrayList<>();
        totalRestaurants.addAll(restaurantsByFood);
        totalRestaurants.addAll(restaurantsByName);

        // Filter out all duplicates results
        List<Restaurant> listWithoutDuplicates = totalRestaurants.stream()
                .distinct()
                .toList();

        List<Food> foodList = listWithoutDuplicates.stream()
                .filter(Objects::nonNull)
                .map(Restaurant::getListFood) // Get food list from each restaurant
                .flatMap(List::stream) // Flatten individual food lists
                .collect(Collectors.toList());

        model.addAttribute("foods", foodList);

        model.addAttribute("restaurants", listWithoutDuplicates);
        return "client/index";
    }
}
