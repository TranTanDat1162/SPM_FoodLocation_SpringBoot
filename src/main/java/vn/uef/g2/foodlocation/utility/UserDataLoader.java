package vn.uef.g2.foodlocation.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.uef.g2.foodlocation.domain.entity.Role;
import vn.uef.g2.foodlocation.domain.entity.User;
import vn.uef.g2.foodlocation.repository.IRoleRepository;
import vn.uef.g2.foodlocation.repository.IUserRepository;

@Component
@RequiredArgsConstructor
public class UserDataLoader implements CommandLineRunner {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.count() == 0) {
            Role roleAdmin = new Role();
            roleAdmin.setRoleName("ADMIN");
            roleRepository.save(roleAdmin);

            Role roleUser = new Role();
            roleUser.setRoleName("USER");
            roleRepository.save(roleUser);
        }
        if(userRepository.count() == 0) {
            User newAdmin = new User();
            newAdmin.setEmail("g2_admin@gmail.com");
            newAdmin.setPassword(passwordEncoder.encode("Admin@123456"));
            newAdmin.setFullName("Admin");
            newAdmin.setRole(roleRepository.findByRoleName("ADMIN"));
            userRepository.save(newAdmin);

            User newUser = new User();
            newUser.setEmail("g2_user@gmail.com");
            newUser.setPassword(passwordEncoder.encode("User@123456"));
            newUser.setFullName("User");
            newUser.setRole(roleRepository.findByRoleName("USER"));
            userRepository.save(newUser);
        }
    }
}
