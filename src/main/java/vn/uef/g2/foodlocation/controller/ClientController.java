package vn.uef.g2.foodlocation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.FoodService;
import vn.uef.g2.foodlocation.service.RestaurantService;

import java.util.List;

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


}
