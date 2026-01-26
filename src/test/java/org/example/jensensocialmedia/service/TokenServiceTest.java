package org.example.jensensocialmedia.service;

import org.example.jensensocialmedia.model.SecurityUser;
import org.example.jensensocialmedia.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @Mock
    private JwtEncoder jwtEncoder;

    @Captor
    private ArgumentCaptor<JwtEncoderParameters> paramsCaptor;

    @InjectMocks
    private TokenService tokenService;

    @Test
    @DisplayName("generateToken should create a JWT token with correct claims")
    void generateTokenSuccess() {
        // Arrange
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(authority); // <-- use wildcard
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("Alice");
        doReturn(authorities).when(auth).getAuthorities(); // to avoid unchecked assignment warning
        when(auth.getPrincipal()).thenReturn("Alice");

        Instant issuedAt = Instant.parse("2024-01-01T00:00:00Z");
        Instant expiresAt = issuedAt.plusSeconds(3600);
        Jwt returned = new Jwt("tokenValue", issuedAt, expiresAt, Map.of("algorithm", "none"), Map.of("sub", "Alice"));
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(returned);

        // Act
        String token = tokenService.generateToken(auth);

        // Assert
        assertEquals("tokenValue", token); // Verify token value
        verify(jwtEncoder).encode(paramsCaptor.capture()); // Capture the argument
        JwtClaimsSet claims = paramsCaptor.getValue().getClaims(); // Extract claims from captured argument
        assertEquals("Alice", claims.getSubject()); // Verify subject
        assertEquals("ROLE_USER", claims.getClaim("scope")); // Verify scope
        assertTrue(claims.getIssuedAt().isBefore(claims.getExpiresAt())); // Verify issuedAt is before expiresAt
        assertEquals(3600, Duration.between(claims.getIssuedAt(), claims.getExpiresAt()).getSeconds()); // Verify duration
    }

    @Test
    @DisplayName("generateToken should handle empty authorities")
    void generateTokenWithEmptyAuthorities() {
        // Arrange
        Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("Bob");
        doReturn(authorities).when(auth).getAuthorities(); // to avoid unchecked assignment warning
        when(auth.getPrincipal()).thenReturn("Bob");

        Instant issuedAt = Instant.parse("2024-01-01T00:00:00Z");
        Instant expiresAt = issuedAt.plusSeconds(3600);
        Jwt returned = new Jwt("tokenValue", issuedAt, expiresAt, Map.of("algorithm", "none"), Map.of("sub", "Bob"));
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(returned);

        // Act
        String token = tokenService.generateToken(auth);

        // Assert
        assertEquals("tokenValue", token); // Verify token value
        verify(jwtEncoder).encode(paramsCaptor.capture()); // Capture the argument
        JwtClaimsSet claims = paramsCaptor.getValue().getClaims(); // Extract claims from captured argument
        assertEquals("Bob", claims.getSubject()); // Verify subject
        assertEquals("", claims.getClaim("scope")); // Verify scope is empty
        assertTrue(claims.getIssuedAt().isBefore(claims.getExpiresAt())); // Verify issuedAt is before expiresAt
        assertEquals(3600, Duration.between(claims.getIssuedAt(), claims.getExpiresAt()).getSeconds()); // Verify duration
    }

    @Test
    @DisplayName("generateToken should handle null authorities")
    void generateTokenWithNullAuthorities() {
        // Arrange
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("Charlie");
        doReturn(null).when(auth).getAuthorities(); // to avoid unchecked assignment warning
        when(auth.getPrincipal()).thenReturn("Charlie");
        Instant issuedAt = Instant.parse("2024-01-01T00:00:00Z");
        Instant expiresAt = issuedAt.plusSeconds(3600);
        Jwt returned = new Jwt("tokenValue", issuedAt, expiresAt, Map.of("algorithm", "none"), Map.of("sub", "Charlie"));
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(returned);
        // Act
        String token = tokenService.generateToken(auth);
        // Assert
        assertEquals("tokenValue", token); // Verify token value
        verify(jwtEncoder).encode(paramsCaptor.capture()); // Capture the argument
        JwtClaimsSet claims = paramsCaptor.getValue().getClaims(); // Extract claims from captured argument
        assertEquals("Charlie", claims.getSubject()); // Verify subject
        assertEquals("", claims.getClaim("scope")); // Verify scope is empty
        assertTrue(claims.getIssuedAt().isBefore(claims.getExpiresAt())); // Verify issuedAt is before expiresAt
        assertEquals(3600, Duration.between(claims.getIssuedAt(), claims.getExpiresAt()).getSeconds()); // Verify duration
    }

    @Test
    @DisplayName("generateToken should handle multiple authorities")
    void generateTokenWithMultipleAuthorities() {
        // Arrange
        Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("Dana");
        doReturn(authorities).when(auth).getAuthorities(); // to avoid unchecked assignment warning
        when(auth.getPrincipal()).thenReturn("Dana");

        Instant issuedAt = Instant.parse("2024-01-01T00:00:00Z");
        Instant expiresAt = issuedAt.plusSeconds(3600);
        Jwt returned = new Jwt("tokenValue", issuedAt, expiresAt, Map.of("algorithm", "none"), Map.of("sub", "Dana"));
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(returned);

        // Act
        String token = tokenService.generateToken(auth);

        // Assert
        assertEquals("tokenValue", token); // Verify token value
        verify(jwtEncoder).encode(paramsCaptor.capture()); // Capture the argument
        JwtClaimsSet claims = paramsCaptor.getValue().getClaims(); // Extract claims from captured argument
        assertEquals("Dana", claims.getSubject()); // Verify subject
        assertEquals("ROLE_USER ROLE_ADMIN", claims.getClaim("scope")); // Verify scope
        assertTrue(claims.getIssuedAt().isBefore(claims.getExpiresAt())); // Verify issuedAt is before expiresAt
        assertEquals(3600, Duration.between(claims.getIssuedAt(), claims.getExpiresAt()).getSeconds()); // Verify duration
    }

    @Test
    @DisplayName("generateToken should handle SecurityUser principal")
    void generateTokenWithSecurityUser() {
        // Arrange
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("Eve");
        doReturn(authorities).when(auth).getAuthorities(); // to avoid unchecked assignment warning
        when(auth.getPrincipal()).thenReturn(new SecurityUser(
                User.builder()
                        .id(42L)
                        .username("Eve")
                        .role("USER")
                        .build()
        ));
        Instant issuedAt = Instant.parse("2024-01-01T00:00:00Z");
        Instant expiresAt = issuedAt.plusSeconds(3600);
        Jwt returned = new Jwt("tokenValue", issuedAt, expiresAt, Map.of("algorithm", "none"), Map.of("sub", "Eve"));
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(returned);

        // Act
        String token = tokenService.generateToken(auth);

        // Assert
        assertEquals("tokenValue", token); // Verify token value
        verify(jwtEncoder).encode(paramsCaptor.capture()); // Capture the argument
        JwtClaimsSet claims = paramsCaptor.getValue().getClaims(); // Extract claims from captured argument
        assertEquals("Eve", claims.getSubject()); // Verify subject
        assertEquals("ROLE_USER", claims.getClaim("scope")); // Verify scope
        assertTrue(claims.getIssuedAt().isBefore(claims.getExpiresAt())); // Verify issuedAt is before expiresAt
        assertEquals(3600, Duration.between(claims.getIssuedAt(), claims.getExpiresAt()).getSeconds()); // Verify duration
    }


}