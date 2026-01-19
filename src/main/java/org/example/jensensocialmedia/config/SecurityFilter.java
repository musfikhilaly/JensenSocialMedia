package org.example.jensensocialmedia.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Filter that processes incoming HTTP requests to validate JWT tokens.
     * If a valid Bearer token is found in the Authorization header, it decodes
     * the token, converts it to an Authentication object, and sets it in the
     * SecurityContext. If the token is invalid or missing, it clears the context
     * and invokes the authentication entry point to handle the error response.
     *
     * @param request     the incoming HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException in case of a servlet error
     * @throws IOException      in case of an I/O error
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String path = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        log.debug("Incoming request: {} {} - Authorization: {}", method, path, authHeader);

        // allow preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            try {
                var jwt = jwtDecoder.decode(token);
                Authentication authentication = jwtAuthenticationConverter.convert(jwt);
                if (authentication == null) {
                    log.debug("JwtAuthenticationConverter returned null for subject={}", jwt.getSubject());
                    SecurityContextHolder.clearContext();
                    authenticationEntryPoint.commence(request, response, new BadCredentialsException("No authorities in token"));
                    return;
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT accepted: subject={}, authorities={}", jwt.getSubject(), authentication.getAuthorities());
            } catch (Exception ex) {
                log.warn("Invalid JWT token: {}", ex.getMessage());
                SecurityContextHolder.clearContext();
                authenticationEntryPoint.commence(request, response, new BadCredentialsException("Invalid JWT token", ex));
                return;
            }
        } else {
            log.debug("No Bearer token present for request {}", path);
        }

        filterChain.doFilter(request, response);
    }
}
