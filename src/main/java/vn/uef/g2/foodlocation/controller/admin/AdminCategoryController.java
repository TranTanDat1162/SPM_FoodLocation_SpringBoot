package vn.uef.g2.foodlocation.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {
    @RequestMapping(value = {"", "/","/list"})
    public String getCategoryList(Model model) {
        return "admin/category/categoryList";
    }

    @RequestMapping(value = "/create")
    public String createNewCategory(Model model) {
        return "admin/category/categoryCreate";
    }
}
