package vn.uef.g2.foodlocation.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.uef.g2.foodlocation.domain.dto.FoodDto;
import vn.uef.g2.foodlocation.domain.dto.RestaurantDto;
import vn.uef.g2.foodlocation.domain.entity.Food;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.FoodService;
import vn.uef.g2.foodlocation.service.RestaurantService;

import java.util.List;

@Controller
@RequestMapping("/admin/food")
@RequiredArgsConstructor
public class AdminFoodController {
    private final FoodService foodService;
    private final RestaurantService restaurantService;
    @GetMapping(value = {"", "/","/list"})
    public String foodList(Model model) {
        List<Food> foodList = foodService.findAll();
        model.addAttribute("foods",foodList);
        return "admin/food/foodList";
    }

    @GetMapping(value ="/create")
    public String showFoodCreate(Model model) {
        // Show drop down list restaurants to create food
        List<Restaurant> restaurantList = restaurantService.findRestaurants();
        model.addAttribute("restaurants", restaurantList);

        model.addAttribute("foodDTO", new FoodDto());
        return "admin/food/foodCreate";
    }

    @PostMapping(value = "/create")
    public String createFood(@Valid FoodDto foodDto, BindingResult result, Model model,
                             RedirectAttributes redirectAttributes) throws FileUploadException {
        // Lấy danh sách nhà hàng từ service
        List<Restaurant> restaurantList = restaurantService.findRestaurants();
        model.addAttribute("restaurants", restaurantList);

        // Kiểm tra nếu có lỗi trong dữ liệu nhập vào
        if (result.hasErrors()) {
            // Đưa danh sách nhà hàng và đối tượng FoodDto có lỗi vào model
            model.addAttribute("foodDto", foodDto);
            // Trả về trang tạo mới món ăn với thông tin nhà hàng và thông tin món ăn đã nhập
            return "admin/food/foodCreate";
        }

        // Nếu không có lỗi, thực hiện thêm món ăn và chuyển hướng đến trang danh sách món ăn
        foodService.add(foodDto);
        redirectAttributes.addFlashAttribute("success_upload", "Create a new food successfully.");
        return "redirect:/admin/food/list";
    }

    @GetMapping("/update/{foodId}")
    public String updateFoodForm(@PathVariable("foodId") Long foodId, Model model) {

        List<Restaurant> restaurantList = restaurantService.findRestaurants();
        Restaurant restaurant = foodService.findRestaurantFromFood(foodId);
        model.addAttribute("restaurants", restaurantList);

        // Lấy thông tin nhà hàng cần cập nhật từ service
        Food food = foodService.findOne(foodId);

        // Chuyển đổi đối tượng Restaurant thành đối tượng RestaurantForm để hiển thị trong form
        FoodDto foodForm = new FoodDto();
        BeanUtils.copyProperties(food, foodForm);

        // Truyền đối tượng RestaurantForm vào model để hiển thị trong form
        model.addAttribute("foodDtoEdit", foodForm);
        model.addAttribute("selectedRestaurantId", restaurant.getId());
        model.addAttribute("prevImageUrl", food.getImage());

        return "admin/food/foodEdit";
    }

    @PostMapping("/update/{foodId}")
    public String updateRestaurant(@PathVariable("foodId") Long foodId,
                                   FoodDto foodForm,
                                   RedirectAttributes redirectAttributes)
            throws FileUploadException {
        // Gọi service để cập nhật thông tin nhà hàng
        foodService.update(foodId, foodForm);
        redirectAttributes.addFlashAttribute("success_upload", "Update food successfully");
        return "redirect:/admin/food/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable Long id,RedirectAttributes ra) {
        foodService.deleteFoodById(id);
        ra.addFlashAttribute("success_delete", "Bạn đã vừa xóa thành công một food.");
        return "redirect:/admin/food/list";
    }
}
