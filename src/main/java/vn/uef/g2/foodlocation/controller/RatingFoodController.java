package vn.uef.g2.foodlocation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.uef.g2.foodlocation.service.FoodService;
import vn.uef.g2.foodlocation.service.RatingFoodService;

@Controller
@RequestMapping(value = "/rating-food")
@RequiredArgsConstructor
public class RatingFoodController {
    private final RatingFoodService ratingFoodService;
    private final FoodService foodService;

    @PostMapping("/add")
    public String addRating(
            @RequestParam("foodId") Long foodId,
            @RequestParam("content") String content,
            @RequestParam("rateStar") int rateStar) {

        //Test
        ratingFoodService.rateFood(content, rateStar, foodId);

        String slug = foodService.findSlugById(foodId);

        return "redirect:/food/" + slug;
    }
}
