package org.example.paiment.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Set<String> roles = Optional.ofNullable(jwt.getClaimAsMap("realm_access"))
                .map(m -> (Collection<?>) m.get("roles"))
                .map(c -> c.stream().map(String::valueOf).collect(Collectors.toSet()))
                .orElseGet(Set::of);

        var authorities = roles.stream()
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        String name = Optional.ofNullable(jwt.getClaimAsString("preferred_username"))
                .orElseGet(() -> Optional.ofNullable(jwt.getClaimAsString("email"))
                        .orElse(jwt.getSubject()));

        return new JwtAuthenticationToken(jwt, authorities, name);
    }
}

