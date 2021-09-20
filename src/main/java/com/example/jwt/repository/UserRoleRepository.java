package com.example.jwt.repository;

import com.example.jwt.domain.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    Optional<UserRole> findDistinctByRoleName(String roleName);
}
