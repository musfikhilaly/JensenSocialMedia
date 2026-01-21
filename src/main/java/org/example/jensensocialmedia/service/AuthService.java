package org.example.jensensocialmedia.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jensensocialmedia.dto.auth.JwtResponseDTO;
import org.example.jensensocialmedia.dto.auth.LoginRequestDTO;
import org.example.jensensocialmedia.model.SecurityUser;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public JwtResponseDTO login(LoginRequestDTO request) {
        MDC.put("requestId", java.util.UUID.randomUUID().toString());
        log.info("Authenticating user: {}", request.username());
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        log.info("Authentication successful for user: {}", request.username());
        String jwt = tokenService.generateToken(auth);
        log.info("Generated JWT token for user: {}", request.username());
        Object principal = auth.getPrincipal();
        log.info("principal class: {}", principal.getClass().getName());

        Long userId = null;
        if (principal instanceof SecurityUser su) {
            log.info("Principal is a SecurityUser, extracting id");
            userId = su.getId();
        } else {
            log.warn("Unhandled principal type: {}", principal.getClass().getName());
            throw new IllegalStateException("Unhandled principal type: " + principal.getClass().getName());
        }

        log.info("User ID extracted: {}", userId);
        return new JwtResponseDTO(jwt, userId);
    }
}
