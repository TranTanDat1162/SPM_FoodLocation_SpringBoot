package vn.uef.g2.foodlocation.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.uef.g2.foodlocation.domain.dto.RatingFoodDto;
import vn.uef.g2.foodlocation.domain.dto.RatingRestaurantDto;
import vn.uef.g2.foodlocation.service.RatingFoodService;
import vn.uef.g2.foodlocation.service.RatingRestaurantService;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/rating-food")
@RequiredArgsConstructor
public class AdminRatingFoodController {
    private final RatingFoodService ratingFoodService;
    @GetMapping("/list")
    public String getList(Model model) {
        List<RatingFoodDto> ratingResponseList = ratingFoodService.findAll();
        long count = ratingResponseList.size();
        double avgRate = ratingResponseList.stream().mapToDouble(RatingFoodDto::getRateStar)
                .average()
                .orElse(0.0);

        model.addAttribute("title", "Rating Food List");
        model.addAttribute("ratingList", ratingResponseList);
        model.addAttribute("count", count);
        model.addAttribute("avgRate", avgRate);
        return "admin/ratingFood/ratingList";
    }


    @PostMapping("/delete/{foodId}")
    public String deleteById(@PathVariable("foodId") Long foodId, RedirectAttributes ra) {
        ratingFoodService.deleteRating(foodId);
        ra.addFlashAttribute("success_delete", "Bạn đã vừa xóa thành công một comment.");
        return "redirect:/admin/rating-food/list";
    }
}
