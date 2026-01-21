package org.example.jensensocialmedia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jensensocialmedia.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

/**
 * Service for generating JWT tokens for authenticated users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        log.info("Generating token for user: {}", authentication.getName());
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Object principal = authentication.getPrincipal();
        Object userIdClaim;
        if (principal instanceof SecurityUser su) {
            userIdClaim = su.getId();
        } else {
            // fallback to subject/username (will be a String)
            userIdClaim = authentication.getName();
        }

        log.info("User ID claim set to: {}", userIdClaim);
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
