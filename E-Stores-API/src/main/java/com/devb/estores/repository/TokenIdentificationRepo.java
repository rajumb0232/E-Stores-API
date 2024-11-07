package com.devb.estores.repository;

import com.devb.estores.model.TokenIdentification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TokenIdentificationRepo extends JpaRepository<TokenIdentification, String> {

    void deleteByExpirationBefore(LocalDateTime localDateTime);
}
