package org.example.jensensocialmedia.repository;

import org.example.jensensocialmedia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
