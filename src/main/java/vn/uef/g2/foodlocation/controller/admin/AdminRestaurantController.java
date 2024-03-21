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
import vn.uef.g2.foodlocation.domain.dto.RestaurantDto;
import vn.uef.g2.foodlocation.domain.entity.Restaurant;
import vn.uef.g2.foodlocation.service.RestaurantService;

import java.util.List;
import java.util.Optional;

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
        RestaurantDto restaurantForm = new RestaurantDto();
        model.addAttribute("restaurantForm", restaurantForm);
        return "admin/restaurant/restaurantCreate";
    }
    @PostMapping("/create")
    public String createRestaurant(@Valid RestaurantDto restaurantForm, BindingResult result, Model model,
                                   RedirectAttributes redirectAttributes) throws FileUploadException {
        if (result.hasErrors()) {
            return "admin/restaurant/restaurantCreate";
        }

        try {
            restaurantService.add(restaurantForm);
        } catch (RestaurantService.DuplicateRestaurantNameException ex) {
            model.addAttribute("duplicateRestaurantNameError", "Tên nhà hàng đã tồn tại");
            model.addAttribute("restaurantForm", restaurantForm); // Giữ lại dữ liệu đã nhập trong form
            return "admin/restaurant/restaurantCreate"; // Trả về trang form với thông báo lỗi
        }
        redirectAttributes.addFlashAttribute("success_upload", "Create a new restaurant successfully.");
        return "redirect:/admin/restaurant/list";
    }

    @GetMapping("/update/{restaurantId}")
    public String updateRestaurantForm(@PathVariable("restaurantId") Long restaurantId, Model model) {

        // Lấy thông tin nhà hàng cần cập nhật từ service
        Restaurant restaurant = restaurantService.findOne(restaurantId)
                .orElseThrow(() -> new RestaurantService
                        .RestaurantNotFoundException("Restaurant not found with id: "
                        + restaurantId));

        // Chuyển đổi đối tượng Restaurant thành đối tượng RestaurantForm để hiển thị trong form
        RestaurantDto updatedRestaurantForm = new RestaurantDto();
        BeanUtils.copyProperties(restaurant, updatedRestaurantForm);

        updatedRestaurantForm.setOpenTime(restaurant.getOpenTime().toString());
        updatedRestaurantForm.setCloseTime(restaurant.getCloseTime().toString());

        // Truyền đối tượng RestaurantForm vào model để hiển thị trong form
        model.addAttribute("res", updatedRestaurantForm);

        return "admin/restaurant/restaurantEdit";
    }

    @PostMapping("/update/{restaurantId}")
    public String updateRestaurant(@PathVariable("restaurantId") Long restaurantId,
                                   RestaurantDto updatedRestaurantForm,
                                   RedirectAttributes redirectAttributes)
            throws FileUploadException {
        // Gọi service để cập nhật thông tin nhà hàng
        restaurantService.update(restaurantId, updatedRestaurantForm);
        redirectAttributes.addFlashAttribute("success_upload", "Update restaurant successfully");
        return "redirect:/admin/restaurant/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable Long id,RedirectAttributes ra) {
        restaurantService.deleteRestaurantById(id);
        ra.addFlashAttribute("success_delete", "Bạn đã vừa xóa thành công một restaurant.");
        return "redirect:/admin/restaurant/list";
    }
}
