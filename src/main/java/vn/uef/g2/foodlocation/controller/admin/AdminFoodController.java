package vn.uef.g2.foodlocation.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/food")
@RequiredArgsConstructor
public class AdminFoodController {
    @GetMapping(value = {"", "/","/list"})
    public String foodList(Model model) {
        return "admin/food/foodList";
    }

    @GetMapping(value ="/create")
    public String foodCreate(Model model) {
        return "admin/food/foodCreate";
    }


}
