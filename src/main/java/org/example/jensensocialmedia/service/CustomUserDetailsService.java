package org.example.jensensocialmedia.service;

import lombok.RequiredArgsConstructor;
import org.example.jensensocialmedia.model.SecurityUser;
import org.example.jensensocialmedia.model.User;
import org.example.jensensocialmedia.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for loading user details for authentication.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads a user by username and returns a SecurityUser object for Spring Security.
     *
     * @param username the username of the user to load
     * @return a SecurityUser object containing user details
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new SecurityUser(user);
    }
}
