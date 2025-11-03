package com.ticketplatform.user.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Extract realm_access.roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
            for (Object role : roles) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
            }
        }

        // Extract resource_access[client].roles (optional)
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            resourceAccess.forEach((client, data) -> {
                if (data instanceof Map<?, ?> clientData && clientData.get("roles") instanceof Collection<?> clientRoles) {
                    for (Object role : clientRoles) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
                    }
                }
            });
        }

        // Extract scopes → SCOPE_xxx
        String scope = jwt.getClaimAsString("scope");
        if (scope != null) {
            for (String s : scope.split("\\s+")) {
                authorities.add(new SimpleGrantedAuthority("SCOPE_" + s));
            }
        }

        String name = Optional.ofNullable(jwt.getClaimAsString("preferred_username"))
                .orElseGet(() -> Optional.ofNullable(jwt.getClaimAsString("email"))
                        .orElse(jwt.getSubject()));

        return new JwtAuthenticationToken(jwt, authorities, name);
    }
}

