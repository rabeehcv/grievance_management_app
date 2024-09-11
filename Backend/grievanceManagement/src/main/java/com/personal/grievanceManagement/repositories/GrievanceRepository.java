package com.personal.grievanceManagement.repositories;

import com.personal.grievanceManagement.entities.Grievance;
import com.personal.grievanceManagement.entities.GrievanceStatus;
import com.personal.grievanceManagement.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrievanceRepository extends JpaRepository<Grievance, Long> {
    List<Grievance> findAllByUser(Users user);
    List<Grievance> findByCategoryIsNull();
    List<Grievance> findByCategoryIsNotNull();
    List<Grievance> findByCategoryIsNotNullAndStatusNot(GrievanceStatus resolved);
    List<Grievance> findAllByAssignee(Users assignee);
}
