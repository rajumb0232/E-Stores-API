package com.devb.estores.repository;

import com.devb.estores.model.RefreshToken;
import com.devb.estores.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String refreshToken);

    Optional<RefreshToken> findByUser(User user);

    List<RefreshToken> findALLByUserAndIsBlocked(User user, boolean isBlocked);

    List<RefreshToken> findAllByExpirationBefore(LocalDateTime now);

    boolean existsByTokenAndIsBlocked(String token, boolean isBlocked);

    List<RefreshToken> findALLByUserAndIsBlockedAndTokenNot(User user, boolean isBlocked, String token);
}
