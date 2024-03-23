package vn.uef.g2.foodlocation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.RatingRestaurantService;
import vn.uef.g2.foodlocation.service.RestaurantService;

@Controller
@RequestMapping(value = "/rating-restaurant")
@RequiredArgsConstructor
public class RatingRestaurantController {
    private final RatingRestaurantService ratingRestaurantService;
    private final RestaurantService restaurantService;
    @PostMapping("/add")
    public String addRating(
            @RequestParam("restaurantId") Long restaurantId,
            @RequestParam("content") String content) {

        //Test
        ratingRestaurantService.rateRestaurant(content, restaurantId);

        String slug = restaurantService.findSlugById(restaurantId);

        return "redirect:/restaurant/" + slug;
    }
}
