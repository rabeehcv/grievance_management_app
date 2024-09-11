package com.personal.grievanceManagement.repositories;

import com.personal.grievanceManagement.entities.Role;
import com.personal.grievanceManagement.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    List<Users> findByRole(Role role);
}
