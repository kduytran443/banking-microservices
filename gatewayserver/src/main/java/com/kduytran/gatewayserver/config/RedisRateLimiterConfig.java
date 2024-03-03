package com.kduytran.gatewayserver.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RedisRateLimiterConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        final int defaultReplenishRate = 1;
        final int defaultBurstCapacity = 1;
        final int defaultRequestedTokens = 1;

        return new RedisRateLimiter(defaultReplenishRate, defaultBurstCapacity, defaultRequestedTokens);
    }

    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous");
    }

}
