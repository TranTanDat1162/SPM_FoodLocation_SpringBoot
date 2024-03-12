package vn.uef.g2.foodlocation.service.imp;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vn.uef.g2.foodlocation.domain.dto.UserDto;

import java.util.List;

public interface IUserService extends UserDetailsService {
    List<UserDto> findAll();


    UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;
}
