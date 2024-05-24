package com.devb.estores.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Random;

@Configuration
public class RandomGenerator {

    @Bean
    public Random random(){
        return new SecureRandom();
    }
}
