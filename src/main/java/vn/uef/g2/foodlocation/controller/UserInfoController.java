package vn.uef.g2.foodlocation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.uef.g2.foodlocation.domain.dto.UserDto;
import vn.uef.g2.foodlocation.service.imp.IUserService;

@ControllerAdvice
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserService userService;
    @ModelAttribute("userInfo")
    public void userInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            UserDto userDto = userService.findByEmail(SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName()).convertUserDto();
            model.addAttribute("userInfo", userDto);
        }
    }
}
