package com.microService.ecommApp.api_gateway.filter;

import com.microService.ecommApp.api_gateway.service.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
//@Component
//@Slf4j
//public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {
//
//    private final JwtService jwtService;
//
//    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
//        super(Config.class);
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//
////            if(!config.isEnabled) return chain.filter(exchange);
//
//            String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//            if (authorizationHeader == null) {
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//
//            String token = authorizationHeader.split("Bearer ")[1];
//
//            Long userId = jwtService.getUserIdFromToken(token);
//
//            exchange.getRequest()
//                    .mutate()
//                    .header("X-User-Id", userId.toString())
//                    .build();
//
//            return chain.filter(exchange);
//        };
//    }
//
//    @Data
//    public static class Config {
//        private boolean isEnabled;
//    }
//}

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            if(!config.isEnabled) return chain.filter(exchange);

            String authorizationHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("Authorization");

            if (authorizationHeader == null ||
                    !authorizationHeader.startsWith("Bearer ")) {

                exchange.getResponse()
                        .setStatusCode(HttpStatus.UNAUTHORIZED);

                return exchange.getResponse().setComplete();
            }

            String token = authorizationHeader.substring(7).trim();

            Long userId = jwtService.getUserIdFromToken(token);

            log.info("Authenticated user id: {}", userId);

            ServerHttpRequest mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId.toString())
                    .build();

            ServerWebExchange mutatedExchange = exchange
                    .mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange);
        };
    }

    @Data
    public static class Config {
        private boolean isEnabled;
    }
}

//Note :>
//You have two different immutable objects involved:
//ServerHttpRequest
//ServerWebExchange
//https://chatgpt.com/c/6a854a38-d738-83e8-a2ce-cb7858b5feb2