package com.programa.pontos.repository;

import com.programa.pontos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByDocument(String document);

    Optional<User> findByUsername(String username);

    User findByDocument(String document);

}
