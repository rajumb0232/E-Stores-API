package com.devb.estores.repository;

import com.devb.estores.enums.TokenType;
import com.devb.estores.model.TokenIdentification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenIdentificationRepo extends JpaRepository<TokenIdentification, String> {

    Optional<TokenIdentification> findByUsernameAndDeviceIdAndTokenType(String username, String deviceId, TokenType tokenType);
    Page<TokenIdentification> findByExpirationBefore(LocalDateTime now, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM token_identifications WHERE expiration < :now LIMIT :batchSize", nativeQuery = true)
    int deleteBatchByExpirationBefore(LocalDateTime now, int batchSize);
}
