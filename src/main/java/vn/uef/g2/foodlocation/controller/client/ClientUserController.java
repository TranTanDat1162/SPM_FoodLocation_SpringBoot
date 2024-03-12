package vn.uef.g2.foodlocation.controller.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ClientUserController {
    @GetMapping("/login")
    public String login() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
//            return "redirect:/users";
//        }
        return "client/login";
    }

    @GetMapping("/register")
    public String showRegisterUser(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
//            return "redirect:/users";
//        }
//        model.addAttribute("user", new SignUpDto());
        return "client/register";
    }

    @RequestMapping(value = {"/users","/users/"})
    public String user(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userEmail = authentication.getName();
//        UserDto user =  userService.findByEmail(userEmail).convertUserDto();
//        model.addAttribute("user", user);
        return "client/userInfo";
    }
}
