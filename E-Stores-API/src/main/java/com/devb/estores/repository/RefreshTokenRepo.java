package com.devb.estores.repository;

import com.devb.estores.security.FingerPrint;
import com.devb.estores.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<FingerPrint, String> {
    Optional<FingerPrint> findByToken(String refreshToken);

    Optional<FingerPrint> findByUser(User user);

    List<FingerPrint> findALLByUserAndIsBlocked(User user, boolean isBlocked);

    List<FingerPrint> findAllByExpirationBefore(LocalDateTime now);

    boolean existsByTokenAndIsBlocked(String token, boolean isBlocked);

    List<FingerPrint> findALLByUserAndIsBlockedAndTokenNot(User user, boolean isBlocked, String token);
}
