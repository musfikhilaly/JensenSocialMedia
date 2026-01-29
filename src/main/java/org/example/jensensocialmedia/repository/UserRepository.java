package org.example.jensensocialmedia.repository;

import org.example.jensensocialmedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository interface for CRUD operations on User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    //we can skip null point exception with Optional

    /**
     * Checks if a user with the given username exists in the database.
     * @param username
     * @return
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user with the given email exists in the database.
     * @param email
     * @return
     */
    boolean existsByEmail(String email);
}

