package vn.uef.g2.foodlocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.uef.g2.foodlocation.domain.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
