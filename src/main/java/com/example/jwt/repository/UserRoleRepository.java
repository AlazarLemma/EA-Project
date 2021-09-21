package com.example.jwt.repository;

import com.example.jwt.domain.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    Optional<UserRole> findDistinctByRoleName(String roleName);

    @Query("SELECT role FROM UserRole role WHERE role.id IN :#{#roleIds}")
    List<UserRole> findMany(List<Long> roleIds);
}
