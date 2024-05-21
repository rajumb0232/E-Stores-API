package com.devb.estores.model;

import com.devb.estores.enums.UserRole;
import com.devb.estores.util.IdGenerator;
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
@ToString
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
