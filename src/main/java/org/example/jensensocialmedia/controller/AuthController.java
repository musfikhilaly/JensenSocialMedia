package org.example.jensensocialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.example.jensensocialmedia.dto.auth.JwtResponseDTO;
import org.example.jensensocialmedia.dto.auth.LonginRequestDTO;
import org.example.jensensocialmedia.service.TokenService;
import org.example.jensensocialmedia.service.UserService;
import org.example.jensensocialmedia.util.CurrentUserProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller responsible for handling authentication requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final CurrentUserProvider currentUserProvider;


    /**
     * Authenticates a user and generates a JWT token upon successful authentication.
     *
     * @param request the login request containing username and password
     * @return a response entity containing the JWT token
     */
    @PostMapping("/session")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LonginRequestDTO request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        Long details = currentUserProvider.getCurrentUserId();

        String jwt = tokenService.generateToken(auth);
        return ResponseEntity.ok(new JwtResponseDTO(jwt, userService.findById(details)));
    }
}