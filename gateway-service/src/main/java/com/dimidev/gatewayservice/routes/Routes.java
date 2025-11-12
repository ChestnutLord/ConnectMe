package com.dimidev.gatewayservice.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return GatewayRouterFunctions.route("gateway")

                // user-service
                .route(
                        RequestPredicates.path("/api/v1/users/**"),
                        HandlerFunctions.http("http://user-service:8080")
                )

                // esteem (match-service)
                .route(
                        RequestPredicates.path("/api/v1/users/*/esteems/**"),
                        HandlerFunctions.http("http://match-service:8080")
                )

                // matches (match-service)
                .route(
                        RequestPredicates.path("/api/v1/users/*/matches/**"),
                        HandlerFunctions.http("http://match-service:8080")
                )

                .build();
    }
}