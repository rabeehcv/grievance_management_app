package com.personal.grievance.repositories;

import com.personal.grievance.entities.Assignee;
import com.personal.grievance.entities.Grievance;
import com.personal.grievance.entities.GrievanceStatus;
import com.personal.grievance.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, Long> {
    List<Grievance> findAllByUser(Users user);
    List<Grievance> findByCategoryIsNull();
    List<Grievance> findByCategoryIsNotNull();
    List<Grievance> findByCategoryIsNotNullAndStatusNot(GrievanceStatus resolved);
    List<Grievance> findAllByAssignee(Assignee assignee);
}
