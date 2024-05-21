package com.devb.estores.model;

import com.devb.estores.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String tokenId;
    private String token;
    private boolean isBlocked;
    private LocalDateTime expiration;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
