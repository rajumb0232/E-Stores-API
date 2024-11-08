package com.devb.estores.util;

import com.devb.estores.repository.TokenIdentificationRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Slf4j
public class ScheduledJobs {

    private final TokenIdentificationRepo tokenIdRepo;

    @Scheduled(fixedRate = 60 * 60 * 1000L)
    public void deleteAllExpiredAccessTokens() {
        log.info("Deleting the token Id's by batch");
        try {
            LocalDateTime now = LocalDateTime.now();
            int batchSize = 1000;
            int deletedCount = 0;
            int page = 0;

            do {
                var pageOfTokens = tokenIdRepo.findByExpirationBefore(now, PageRequest.of(page, batchSize));

                if (!pageOfTokens.isEmpty()) {
                    tokenIdRepo.deleteAll(pageOfTokens);
                    deletedCount = pageOfTokens.getNumberOfElements();
                    log.info("Deleted {} expired access tokens in this batch.", deletedCount);
                }

                page++;
            } while (deletedCount == batchSize);

            log.info("Completed deleting expired token Id's at {}", now);
        } catch (Exception e) {
            log.error("Error occurred while deleting expired token Id's: ", e);
        }
    }
}
