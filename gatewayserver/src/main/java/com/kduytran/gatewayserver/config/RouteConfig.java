package com.kduytran.gatewayserver.config;

import com.kduytran.gatewayserver.utils.PathUtils;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@Configuration
public class RouteConfig {

    private final RedisRateLimiter redisRateLimiter;
    private final KeyResolver userKeyResolver;

    @Autowired
    public RouteConfig(RedisRateLimiter redisRateLimiter, KeyResolver userKeyResolver) {
        this.redisRateLimiter = redisRateLimiter;
        this.userKeyResolver = userKeyResolver;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path(PathUtils.getPathWithContextPath("accounts/**"))
                        .filters(f -> f
                                .rewritePath(PathUtils.getPathWithContextPath("accounts/(?<segment>.*)"), "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker")
                                        .setFallbackUri("forward:/contactSupport")
                                )
                                .retry(retryConfig -> retryConfig
                                        .setRetries(2)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        )
                        .uri("lb://ACCOUNTS"))
                .route(p -> p
                        .path(PathUtils.getPathWithContextPath("cards/**"))
                        .filters(f -> f
                                .rewritePath(PathUtils.getPathWithContextPath("cards/(?<segment>.*)"), "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .requestRateLimiter(config -> config
                                        .setRateLimiter(redisRateLimiter)
                                        .setKeyResolver(userKeyResolver))
                        )
                        .uri("lb://CARDS"))
                .route(p -> p
                        .path(PathUtils.getPathWithContextPath("loans/**"))
                        .filters(f -> f
                                .rewritePath(PathUtils.getPathWithContextPath("loans/(?<segment>.*)"), "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri("lb://LOANS"))
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
    }


}
