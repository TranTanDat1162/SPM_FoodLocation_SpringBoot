package vn.uef.g2.foodlocation.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/admin/restaurant")
public class AdminRestaurantController {
    @GetMapping(value = {"", "/","/list"})
    public String restaurantsList(Model model) {
//        List<Restaurant> restaurants = restaurantService.findRestaurants();
//        model.addAttribute("res", restaurants);
        return "admin/restaurant/restaurantList";
    }

    @GetMapping("/create")
    public String createRestaurant(Model model) {
//        log.info("in get create");
//        RestaurantForm restaurantForm = new RestaurantForm();
//        model.addAttribute("restaurantForm", restaurantForm);

        return "admin/restaurant/restaurantCreate";
    }
}
