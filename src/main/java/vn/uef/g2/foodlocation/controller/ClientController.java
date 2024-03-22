package vn.uef.g2.foodlocation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.uef.g2.foodlocation.domain.dto.RestaurantDto;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.FoodService;
import vn.uef.g2.foodlocation.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final RestaurantService restaurantService;
    private final FoodService foodService;

    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        List<Restaurant> restaurants = restaurantService.findRestaurants();
        List<Food> foodList = foodService.findAll();
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("foods", foodList);
        return "client/index";
    }

    @GetMapping(value = {"/search"})
    public String getKeyword(@RequestParam String keyword,String district, String radius, Model model) {
        List<Restaurant> restaurantsByName = restaurantService.findMatchingRestaurant(keyword);
        Set<Restaurant> restaurantsByFood = restaurantService.findAllRestaurantsWithFoodName(keyword);

        List<Restaurant> totalRestaurants = new ArrayList<>();
        totalRestaurants.addAll(restaurantsByFood);
        totalRestaurants.addAll(restaurantsByName);

        List<Restaurant> listWithoutDuplicates = totalRestaurants.stream()
                .distinct()
                .toList();

        List<Food> foodList = listWithoutDuplicates.stream()
                .filter(Objects::nonNull) // Optional null check
                .map(Restaurant::getListFood) // Get food list from each restaurant
                .flatMap(List::stream) // Flatten individual food lists
                .collect(Collectors.toList());

        model.addAttribute("foods", foodList);

        model.addAttribute("restaurants", listWithoutDuplicates);
        return "client/index";
    }
}
