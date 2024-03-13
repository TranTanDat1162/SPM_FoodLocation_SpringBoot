package vn.uef.g2.foodlocation.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.uef.g2.foodlocation.domain.dto.SignUpDto;
import vn.uef.g2.foodlocation.domain.dto.UserDto;
import vn.uef.g2.foodlocation.domain.entity.User;
import vn.uef.g2.foodlocation.repository.IRoleRepository;
import vn.uef.g2.foodlocation.repository.IUserRepository;

@Controller
@RequiredArgsConstructor
public class ClientUserController {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        model.addAttribute("user", new SignUpDto());
        return "client/register";
    }
    @PostMapping("client/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid SignUpDto signUpDto,
                                            HttpServletRequest request, Errors errors) {
        User newUser = new User();
        newUser.setEmail(signUpDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        newUser.setFullName(signUpDto.getFullName());
        newUser.setRole(roleRepository.findByRoleName("USER"));
        userRepository.save(newUser);
        return "redirect:/users";
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
