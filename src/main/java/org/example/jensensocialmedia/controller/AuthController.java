package org.example.jensensocialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.example.jensensocialmedia.dto.auth.JwtResponseDTO;
import org.example.jensensocialmedia.dto.auth.LoginRequestDTO;
import org.example.jensensocialmedia.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling authentication requests.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    /**
     * Authenticates a user and generates a JWT token upon successful authentication.
     *
     * @param request the login request containing username and password
     * @return a response entity containing the JWT token
     */
    @PostMapping("/request-token")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO request) {

        return ResponseEntity.ok().body(authService.login(request));
    }
}