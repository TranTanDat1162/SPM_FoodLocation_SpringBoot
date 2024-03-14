package vn.uef.g2.foodlocation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.uef.g2.foodlocation.domain.dto.SignUpDto;
import vn.uef.g2.foodlocation.domain.dto.UserDto;
import vn.uef.g2.foodlocation.domain.entity.User;
import vn.uef.g2.foodlocation.repository.IRoleRepository;
import vn.uef.g2.foodlocation.repository.IUserRepository;
import vn.uef.g2.foodlocation.service.imp.IUserService;
import vn.uef.g2.foodlocation.utility.UploadFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy dữ liệu tài khoản");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                List.of(authority));
    }

    @Override
    public User save(SignUpDto user) {
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("Email đã tồn tại");
            }
            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setFullName(user.getFullName());
            newUser.setRole(roleRepository.findByRoleName("User"));
            return userRepository.save(newUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy dữ liệu tài khoản");
        }
        return user;
    }

    @Override
    public void uploadAvatar(MultipartFile avatar, String email) {
        User user = userRepository.findByEmail(email);
        String currentAvatar = user.getAvatar();
        try {
            user.setAvatar(UploadFile.uploadFile(avatar));
            userRepository.save(user);
            if(currentAvatar != null){
                UploadFile.deleteFile(currentAvatar);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void userUpdate(UserDto userDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        user.setFullName(userDto.getFullName());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        userRepository.save(user);
    }

    @Override
    public boolean checkExistPassword(String password, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void changePassword(String password, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto resultUser = new UserDto();
            resultUser.setId(user.getId());
            resultUser.setFullName(user.getFullName());
            resultUser.setPhone(user.getPhone());
            resultUser.setEmail(user.getEmail());
            resultUser.setAddress(user.getAddress());
            resultUser.setAvatar(user.getAvatar());
            resultUser.setRole(user.getRole().getRoleName());
            resultUser.setCreatedAt(user.getCreatedAt());
            userDtos.add(resultUser);
        }
        return userDtos;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByEmail(username);
    }

}
