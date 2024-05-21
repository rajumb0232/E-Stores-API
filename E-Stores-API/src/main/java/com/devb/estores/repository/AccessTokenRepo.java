package com.devb.estores.repository;

import com.devb.estores.model.AccessToken;
import com.devb.estores.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccessTokenRepo extends JpaRepository<AccessToken, String> {
    Optional<AccessToken> findByToken(String accessToken);

    Optional<AccessToken> findByUser(User user);

    List<AccessToken> findAllByUserAndIsBlocked(User user, boolean isBlocked);

    List<AccessToken> findAllByExpirationBefore(LocalDateTime now);

    boolean existsByTokenAndIsBlocked(String token, boolean isBlocked);
}
