package vn.uef.g2.foodlocation.domain.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import vn.uef.g2.foodlocation.domain.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Getter @Setter
@Table(name = "user" )
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "full_name" , nullable = false)
    private String fullName;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name= "avatar")
    private String avatar;
    @Column(name="create_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RatingRestaurant> listRatingRestaurant = new ArrayList<>();

    @OneToMany(mappedBy = "favorite_restaurant_id", cascade = CascadeType.ALL)
    private List<FavoriteRestaurant> listFavoriteRestaurant = new ArrayList<>();

    public UserDto convertUserDto(){
        UserDto userDto = new UserDto();
        userDto.setId(this.id);
        userDto.setFullName(this.fullName);
        userDto.setEmail(this.email);
        userDto.setPhone(this.phone);
        userDto.setAddress(this.address);
        userDto.setAvatar(this.avatar);
        userDto.setCreatedAt(this.createdAt);
        userDto.setRole(this.role.getRoleName());
        return userDto;
    }
}
