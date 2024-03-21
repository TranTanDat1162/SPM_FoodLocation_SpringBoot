package vn.uef.g2.foodlocation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.RestaurantService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientController {
    private final RestaurantService restaurantService;

    @GetMapping(value = {"", "/"})
    public String home(Model model) {
        List<Restaurant> restaurants = restaurantService.findRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "client/index";
    }
}
