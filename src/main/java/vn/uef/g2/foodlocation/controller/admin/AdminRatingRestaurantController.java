package vn.uef.g2.foodlocation.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/rating-restaurant")
public class AdminRatingRestaurantController {
    @GetMapping(value = {"", "/","/list"})
    public String getList(Model model) {
//        List<RatingRestaurantDto> ratingResponseList= ratingRestaurantService.findAll();
//        long count = ratingResponseList.size();
//        double avgRate = ratingResponseList.stream().mapToDouble(RatingRestaurantDto::getRateStar)
//                .average()
//                .orElse(0.0);
//        model.addAttribute("title", "Rating Restaurant List");
//        model.addAttribute("ratingList", ratingResponseList);
//        model.addAttribute("count", count);
//        model.addAttribute("avgRate", avgRate);
        return "admin/ratingRestaurant/ratingRestaurantList";
    }

}
