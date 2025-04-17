package com.bmw.store.Repositories;

import com.bmw.store.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByfirstName(String firstName);

    Optional<User> findBylastName(String lastName);

    Optional<User> findByemail(String email);

}
