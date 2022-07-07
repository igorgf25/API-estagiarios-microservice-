package com.igor.gateway.security;

import com.igor.gateway.UserDto.LoginDTO;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Token de autorização não informado");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String[] headerParts = authHeader.split(" ");

            if (headerParts.length != 2 || !"Bearer".equals(headerParts[0])) {
                throw new RuntimeException("Estrutura de autorização incorreta");
            }

            try {
                return webClientBuilder.build()
                        .post()
                        .uri("http://auth:8085/validateToken?token=" + headerParts[1])
                        .retrieve()
                        .bodyToMono(LoginDTO.class)
                        .map(loginDTO -> {
                            exchange.getRequest()
                                    .mutate()
                                    .header("X-auth-user-id", String.valueOf(loginDTO.getId()));
                            return exchange;
                        }).flatMap(chain::filter);
            } catch (Exception e) {
                return null;
            }

        };
    }

    public static class Config {
        // empty class as I don't need any particular configuration
    }
}
