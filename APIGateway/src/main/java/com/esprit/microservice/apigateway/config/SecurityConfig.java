package com.esprit.microservice.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.disable())
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/users/register").permitAll()
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**").permitAll()
                        .pathMatchers("/api/users/**").hasAnyRole("admin")
                        .pathMatchers("/Vehicules/api/vehicle/**").hasAnyRole("admin", "driver")
                        .pathMatchers("/conge/**").hasAnyRole("admin", "driver")
                        .pathMatchers("/api/fastpost/**").hasAnyRole("admin", "driver", "client")
                        .pathMatchers("/api/delivery-fastpost/**").hasAnyRole("admin", "driver", "client")
                        .pathMatchers("/api/delivery/**").hasAnyRole("admin", "driver", "client")
                        .pathMatchers("/complaint/**").hasAnyRole("admin", "driver", "client")
                        .pathMatchers("/api/carpooling/**").hasAnyRole("admin", "driver", "client")
                        .pathMatchers("/paiment/**").hasAnyRole("admin", "driver", "client")
                        .pathMatchers("/api/Produit/**").hasAnyRole("admin", "driver", "client")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthConverter()))
                )
                .build();
    }

    /**
     * Convertit les rôles Keycloak (realm_access.roles) en authorities Spring ("ROLE_*")
     * et adapte le retour pour WebFlux (Mono<AbstractAuthenticationToken>)
     */
    private Converter<Jwt, Mono<JwtAuthenticationToken>> keycloakJwtAuthConverter() {
        return jwt -> {
            // Extraire les rôles du token (dans realm_access)
            Set<String> roles = Optional.ofNullable(jwt.getClaimAsMap("realm_access"))
                    .map(m -> (Collection<?>) m.get("roles"))
                    .map(c -> c.stream().map(String::valueOf).collect(Collectors.toSet()))
                    .orElseGet(Set::of);

            // Préfixer les rôles avec "ROLE_"
            var authorities = roles.stream()
                    .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            // Identifier l’utilisateur (preferred_username ou email)
            String name = Optional.ofNullable(jwt.getClaimAsString("preferred_username"))
                    .orElseGet(() -> Optional.ofNullable(jwt.getClaimAsString("email"))
                            .orElse(jwt.getSubject()));

            // Retourner un Mono (version réactive)
            return Mono.just(new JwtAuthenticationToken(jwt, authorities, name));
        };
    }
}
