package com.self.flipcart.repository;

import com.self.flipcart.model.RefreshToken;
import com.self.flipcart.model.User;
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
}
