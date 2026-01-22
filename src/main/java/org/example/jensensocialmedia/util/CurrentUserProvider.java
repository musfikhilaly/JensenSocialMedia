package org.example.jensensocialmedia.util;

import lombok.extern.slf4j.Slf4j;
import org.example.jensensocialmedia.exception.UnauthorizedException;
import org.example.jensensocialmedia.model.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CurrentUserProvider {
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            // Handle unauthenticated user case as needed
            log.warn("Unauthenticated access attempt");
            throw new UnauthorizedException();
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof SecurityUser su) {
            return su.getId();
        }

        if (principal instanceof Jwt jwt) {
            Object claim = jwt.getClaims().get("user_id");
            if (claim instanceof Number) {
                return ((Number) claim).longValue();
            } else if (claim instanceof String) {
                try {
                    return Long.parseLong((String) claim);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            }
            String sub = jwt.getSubject();
            try {
                return Long.parseLong(sub);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }

        throw new UnauthorizedException();
    }
}
