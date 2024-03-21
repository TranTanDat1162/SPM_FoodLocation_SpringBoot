package vn.uef.g2.foodlocation.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.uef.g2.foodlocation.domain.dto.CreateRestaurantDTO;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.RestaurantService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/restaurant")
public class AdminRestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping(value = {"", "/","/list"})
    public String restaurantsList(Model model) {
        List<Restaurant> restaurants = restaurantService.findRestaurants();
        model.addAttribute("res", restaurants);
        return "admin/restaurant/restaurantList";
    }

    @GetMapping("/create")
    public String showCreateRestaurantForm(Model model) {
//        log.info("in get create");
        CreateRestaurantDTO restaurantForm = new CreateRestaurantDTO();
        model.addAttribute("restaurantForm", restaurantForm);

        return "admin/restaurant/restaurantCreate";
    }
    @PostMapping("/create")
    public String createRestaurant(@Valid CreateRestaurantDTO restaurantForm, BindingResult result,
                                   RedirectAttributes redirectAttributes) throws FileUploadException {
        if (result.hasErrors()) {
            return "admin/restaurant/restaurantCreate";
        }

        restaurantService.add(restaurantForm);
        return "redirect:/admin/restaurant/list";
    }
}
