package com.kduytran.gatewayserver.config;

import com.kduytran.gatewayserver.utils.PathUtils;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path(PathUtils.getPathWithContextPath("accounts/**"))
                        .filters(f -> f
                                .rewritePath(PathUtils.getPathWithContextPath("accounts/(?<segment>.*)"), "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri("lb://ACCOUNTS"))
                .route(p -> p
                        .path(PathUtils.getPathWithContextPath("cards/**"))
                        .filters(f -> f
                                .rewritePath(PathUtils.getPathWithContextPath("cards/(?<segment>.*)"), "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
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

}
