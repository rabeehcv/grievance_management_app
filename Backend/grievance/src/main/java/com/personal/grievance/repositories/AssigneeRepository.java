package com.personal.grievance.repositories;

import com.personal.grievance.entities.Assignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssigneeRepository extends JpaRepository<Assignee, Long> {
    Optional<Assignee> findByEmail(String email);
}
