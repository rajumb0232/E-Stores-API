package com.self.flipcart.model;

import com.self.flipcart.enums.UserRole;
import com.self.flipcart.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String userId;
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private List<UserRole> roles;
    private boolean isEmailVerified;
}
