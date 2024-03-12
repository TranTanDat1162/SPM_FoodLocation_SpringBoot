package vn.uef.g2.foodlocation.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.uef.g2.foodlocation.domain.dto.UserDto;
import vn.uef.g2.foodlocation.service.imp.IUserService;

import java.util.List;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final IUserService userService;

    @RequestMapping(value = {"", "/","/list"})
    public String usersList(Model model) {

        List<UserDto> users = userService.findAll();
        /*order id user and reverse*/
        users.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));

        model.addAttribute("users", users);

        return "admin/user/userList";
    }
}
