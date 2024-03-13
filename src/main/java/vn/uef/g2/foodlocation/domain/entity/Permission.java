package vn.uef.g2.foodlocation.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_name")
    private String permissionName;

    @ManyToMany(mappedBy = "permissions", cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();
}
