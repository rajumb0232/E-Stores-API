package com.self.flipcart.util;

import com.self.flipcart.model.AccessToken;
import com.self.flipcart.model.RefreshToken;
import com.self.flipcart.repository.AccessTokenRepo;
import com.self.flipcart.repository.RefreshTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ScheduledJobs {

    private AccessTokenRepo accessTokenRepo;
    private RefreshTokenRepo refreshTokenRepo;

    @Scheduled(fixedDelay = 60 * 60 * 1000l)
    public void deleteAllExpiredAccessTokens() {
        List<AccessToken> ats = accessTokenRepo.findAllByExpirationBefore(LocalDateTime.now());
        if(ats!=null && !ats.isEmpty()) accessTokenRepo.deleteAll(ats);
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000l)
    public void deleteAllExpiredRefreshTokens() {
        List<RefreshToken> rts =  refreshTokenRepo.findAllByExpirationBefore(LocalDateTime.now());
        if(rts!=null && !rts.isEmpty()) refreshTokenRepo.deleteAll(rts);
    }
}
