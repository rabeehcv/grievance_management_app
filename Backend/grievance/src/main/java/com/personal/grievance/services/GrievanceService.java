package com.personal.grievance.services;

import com.personal.grievance.entities.*;
import com.personal.grievance.repositories.AssigneeRepository;
import com.personal.grievance.repositories.GrievanceRepository;
import com.personal.grievance.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GrievanceService {
    @Autowired
    private GrievanceRepository grievanceRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AssigneeRepository assigneeRepository;

    public Grievance createGrievance(String title, String description, Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Grievance grievance = new Grievance();
        grievance.setTitle(title);
        grievance.setDescription(description);
        grievance.setStatus(GrievanceStatus.PENDING);
        grievance.setSubmitted_on(LocalDateTime.now());
        grievance.setUser(user);
        return grievanceRepository.save(grievance);
    }
    public List<Grievance> getAllGrievancesByUserId(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found"));
        return grievanceRepository.findAllByUser(user);
    }
    public List<Grievance> getAllUnassignedGrievances() {
        return grievanceRepository.findByCategoryIsNull();
    }
    public Grievance assignGrievance(Long grievanceId, Long assigneeId, GrievanceCategory grievanceCategory) {
        Assignee assignee = assigneeRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found."));
        Grievance grievance = grievanceRepository.findById(grievanceId)
                .orElseThrow(() -> new RuntimeException("Grievance not found."));
        grievance.setAssignee(assignee);
        grievance.setCategory(grievanceCategory);
        return grievanceRepository.save(grievance);
    }
    public List<Grievance> getAllAssignedGrievances(){
       return grievanceRepository.findByCategoryIsNotNull();
    }
    public List<Grievance> getAllUnresolvedGrievances(){
        return grievanceRepository.findByCategoryIsNotNullAndStatusNot(GrievanceStatus.RESOLVED);
    }
    public List<Grievance> getAllGrievancesByAssigneeId(Long assigneeId){
        Assignee assignee = assigneeRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
        return grievanceRepository.findAllByAssignee(assignee);
    }
    public Grievance updateGrievanceStatus(Long grievanceId, GrievanceStatus grievanceStatus){
        Grievance grievance = grievanceRepository.findById(grievanceId)
                .orElseThrow(() -> new RuntimeException("Grievance not found"));
        grievance.setStatus(grievanceStatus);
        return grievanceRepository.save(grievance);
    }
}
