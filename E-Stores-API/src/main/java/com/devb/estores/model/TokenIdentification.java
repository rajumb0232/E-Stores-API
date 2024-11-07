package com.devb.estores.model;

import com.devb.estores.enums.TokenType;
import com.devb.estores.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "token_identifications")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenIdentification {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String tokenId;
    private String username;
    private String deviceId;
    private TokenType tokenType;
    private String jti;
    private LocalDateTime expiration;
}
