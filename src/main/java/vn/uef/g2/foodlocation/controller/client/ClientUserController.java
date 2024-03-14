package vn.uef.g2.foodlocation.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.uef.g2.foodlocation.domain.dto.SignUpDto;
import vn.uef.g2.foodlocation.domain.dto.UserDto;
import vn.uef.g2.foodlocation.domain.entity.User;
import vn.uef.g2.foodlocation.repository.IRoleRepository;
import vn.uef.g2.foodlocation.repository.IUserRepository;
import vn.uef.g2.foodlocation.service.imp.IUserService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientUserController {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserService userService;

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/users";
        }
        return "client/login";
    }

    @GetMapping("/register")
    public String showRegisterUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/users";
        }

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserDto user =  userService.findByEmail(userEmail).convertUserDto();
        model.addAttribute("user", user);
        return "client/userInfo";
    }

    @PostMapping("/users/change_password")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @RequestParam("oldPassword") String oldPassword,
                                 RedirectAttributes ra
    ) {
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("error_change", "Mật khẩu xác nhận không khớp.");
            return "redirect:/users?confirmPasswordError";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        if (!userService.checkExistPassword(oldPassword,userEmail)) {
            ra.addFlashAttribute("error_change", "Mật khẩu cũ không đúng.");
            return "redirect:/users?oldPasswordError";
        }
        if(userService.checkExistPassword(newPassword,userEmail)){
            ra.addFlashAttribute("error_change", "Mật khẩu mới không được trùng với mật khẩu cũ.");
            return "redirect:/users?existPasswordError";
        }
        userService.changePassword(newPassword, userEmail);
        ra.addFlashAttribute("success_change", "Đổi mật khẩu thành công");
        return "redirect:/users?changePasswordSuccess";
    }

    @PostMapping("/users/update")
    public String update(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, RedirectAttributes ra) {
        try{
            if (bindingResult.hasErrors()) {
                return "client/userInfo";
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            userService.userUpdate(userDto, userEmail);
            ra.addFlashAttribute("success_update","Cập nhật thông tin thành công.");
            return "redirect:/users?updateSuccess";
        }catch (Exception e){
            ra.addFlashAttribute("error_update","Cập nhật thông tin thất bại.");
            return "redirect:/users?updateError";
        }
    }

    @PostMapping("/users/upload_avatar")
    public String uploadAvatar(@RequestParam("file") MultipartFile file, RedirectAttributes ra) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
//        Validate image
        List<String> validContentTypes = Arrays.asList("image/jpeg", "image/png");
        if(!validContentTypes.contains(file.getContentType())){
            ra.addFlashAttribute("error_upload", "Ảnh không đúng định dạng.");
            return "redirect:/users";
        }
        if(file.getSize() > 5000000){
            ra.addFlashAttribute("error_upload", "Kích thước ảnh không được vượt quá 5MB.");
            return "redirect:/users?uploadErrorSize";
        }
        userService.uploadAvatar(file, userEmail);
        ra.addFlashAttribute("success_upload", "Cập nhật avatar thành công.");
        return "redirect:/users?uploadSuccess";
    }

    @GetMapping("/logout")
    public String logOutCallback() {
        return "redirect:/users";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
