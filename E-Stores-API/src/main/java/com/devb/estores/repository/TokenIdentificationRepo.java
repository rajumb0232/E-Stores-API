package com.devb.estores.repository;

import com.devb.estores.enums.TokenType;
import com.devb.estores.model.TokenIdentification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenIdentificationRepo extends JpaRepository<TokenIdentification, String> {

    Optional<TokenIdentification> findByUsernameAndDeviceIdAndTokenType(String username, String deviceId, TokenType tokenType);
    Page<TokenIdentification> findByExpirationBefore(LocalDateTime now, Pageable pageable);

    void deleteByUsernameAndDeviceIdAndTokenType(String username, String deviceId, TokenType tokenType);

    List<TokenIdentification> findAllByUsernameAndDeviceIdNot(String username, String currentDeviceId);

    List<TokenIdentification> findAllByUsername(String username);
}
