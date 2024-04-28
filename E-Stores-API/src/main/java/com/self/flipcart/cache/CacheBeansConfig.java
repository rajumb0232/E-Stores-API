package com.self.flipcart.cache;

import com.self.flipcart.dto.Attempt;
import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheBeansConfig {

    @Bean
    CacheStore<OtpModel> otpCache(){
        return new CacheStore<>(Duration.ofMinutes(5));
    }

    @Bean
    CacheStore<User> userCacheStore(){
        return new CacheStore<>(Duration.ofMinutes(10));
    }

    @Bean
    CacheStore<Attempt> attemptCacheStore(){
        return new CacheStore<>(Duration.ofMinutes(1));
    }
}
