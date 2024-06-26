package vn.uef.g2.foodlocation.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.uef.g2.foodlocation.domain.dto.RatingRestaurantDto;
import vn.uef.g2.foodlocation.service.RatingRestaurantService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/rating-restaurant")
public class AdminRatingRestaurantController {
    private final RatingRestaurantService ratingRestaurantService;
    @GetMapping(value = {"", "/","/list"})
    public String getList(Model model) {
        List<RatingRestaurantDto> ratingResponseList= ratingRestaurantService.findAll();
        long count = ratingResponseList.size();
        model.addAttribute("title", "Rating Restaurant List");
        model.addAttribute("ratingList", ratingResponseList);
        model.addAttribute("count", count);
        return "admin/ratingRestaurant/ratingRestaurantList";
    }

    @PostMapping(value = "/delete/{id}")
    public String deleteRestaurant(@PathVariable Long id, RedirectAttributes ra) {
        ratingRestaurantService.deleteRating(id);
        ra.addFlashAttribute("success_delete", "Bạn đã vừa xóa thành công một comment.");
        return "redirect:/admin/rating-restaurant/list";
    }
}
