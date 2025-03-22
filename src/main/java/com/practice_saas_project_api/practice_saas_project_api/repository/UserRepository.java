package com.practice_saas_project_api.practice_saas_project_api.repository;

import com.practice_saas_project_api.practice_saas_project_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
