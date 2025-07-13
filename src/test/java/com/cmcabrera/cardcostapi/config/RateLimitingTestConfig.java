package com.cmcabrera.cardcostapi.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;

@Profile("test")
@Configuration
public class RateLimitingTestConfig {

    @Bean
    public Bucket paymentCardsCostBucket() {
        // For testing, set a very low limit to easily trigger rate limiting
        Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Bean
    public Bucket authenticationBucket() {
        // For testing, set a very low limit to easily trigger rate limiting
        Bandwidth limit = Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
