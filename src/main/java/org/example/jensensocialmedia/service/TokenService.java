package org.example.jensensocialmedia.service;

import lombok.RequiredArgsConstructor;
import org.example.jensensocialmedia.model.SecurityUser;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Service for generating JWT tokens for authenticated users.
 */
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String scope = (authorities == null) ? "" : authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Object principal = authentication.getPrincipal();
        Object userIdClaim;
        if (principal instanceof SecurityUser su) {
            userIdClaim = su.getId();
        } else {
            throw new AuthenticationServiceException(
                    "Unexpected principal type when generating token: " + principal.getClass().getName()
            );
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("user_id", userIdClaim)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
