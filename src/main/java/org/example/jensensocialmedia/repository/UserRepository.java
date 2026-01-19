package org.example.jensensocialmedia.repository;

import org.example.jensensocialmedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    //we can skip null point exception with Optional

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

