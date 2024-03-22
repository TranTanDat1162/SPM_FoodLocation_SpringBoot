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
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.RestaurantService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        model.addAttribute("restaurants", listWithoutDuplicates);
        return "client/index";
    }
}