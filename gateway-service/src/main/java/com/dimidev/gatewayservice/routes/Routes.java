package com.dimidev.gatewayservice.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.rewritePath;

@Configuration
public class Routes {

    // USER-SERVICE

    @Bean
    public RouterFunction<ServerResponse> userServiceRoutes() {
        return GatewayRouterFunctions.route("user_service")
                .route(
                        RequestPredicates.path("/api/v1/users/**"),
                        HandlerFunctions.http("http://user-service:8080")
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("user_service_swagger")
                .route(
                        RequestPredicates.path("/aggregate/user-service/v3/api-docs"),
                        HandlerFunctions.http("http://user-service:8080")
                )
                .filter(
                        rewritePath(
                                "/aggregate/user-service/v3/api-docs",
                                "/v3/api-docs"
                        )
                )
                .build();
    }

    // MATCH-SERVICE

    @Bean
    public RouterFunction<ServerResponse> matchEsteemRoutes() {
        return GatewayRouterFunctions.route("match_service_esteems")
                .route(
                        RequestPredicates.path("/api/v1/users/*/esteems/**"),
                        HandlerFunctions.http("http://match-service:8080")
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> matchMatchesRoutes() {
        return GatewayRouterFunctions.route("match_service_matches")
                .route(
                        RequestPredicates.path("/api/v1/users/*/matches/**"),
                        HandlerFunctions.http("http://match-service:8080")
                )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> matchServiceSwaggerRoute() {
        return GatewayRouterFunctions.route("match_service_swagger")
                .route(
                        RequestPredicates.path("/aggregate/match-service/v3/api-docs"),
                        HandlerFunctions.http("http://match-service:8080")
                )
                .filter(
                        rewritePath(
                                "/aggregate/match-service/v3/api-docs",
                                "/v3/api-docs"
                        )
                )
                .build();
    }

}
