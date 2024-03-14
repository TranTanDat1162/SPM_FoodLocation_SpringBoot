package vn.uef.g2.foodlocation.service.imp;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import vn.uef.g2.foodlocation.domain.dto.SignUpDto;
import vn.uef.g2.foodlocation.domain.dto.UserDto;
import vn.uef.g2.foodlocation.domain.entity.User;

import java.util.List;

public interface IUserService extends UserDetailsService {
    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;

    User save(SignUpDto user);

    User findByEmail(String userEmail);

    void uploadAvatar(MultipartFile avatar, String email);

    void userUpdate(UserDto userDto, String userEmail);

    boolean checkExistPassword(String password, String userEmail);

    void changePassword(String password, String userEmail);
    List<UserDto> findAll();
}
