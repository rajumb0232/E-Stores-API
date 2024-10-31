package com.devb.estores.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ScheduledJobs {

//    private AccessTokenRepo accessTokenRepo;
//    private RefreshTokenRepo refreshTokenRepo;

//    @Scheduled(fixedDelay = 60 * 60 * 1000l)
//    public void deleteAllExpiredAccessTokens() {
//        List<AccessToken> ats = accessTokenRepo.findAllByExpirationBefore(LocalDateTime.now());
//        if(ats!=null && !ats.isEmpty()) accessTokenRepo.deleteAll(ats);
//    }
//
//    @Scheduled(fixedDelay = 60 * 60 * 1000l)
//    public void deleteAllExpiredRefreshTokens() {
//        List<FingerPrint> rts =  refreshTokenRepo.findAllByExpirationBefore(LocalDateTime.now());
//        if(rts!=null && !rts.isEmpty()) refreshTokenRepo.deleteAll(rts);
//    }
}
