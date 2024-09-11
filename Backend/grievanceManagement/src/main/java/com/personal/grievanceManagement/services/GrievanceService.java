package com.personal.grievanceManagement.services;

import com.personal.grievanceManagement.entities.Grievance;
import com.personal.grievanceManagement.entities.GrievanceCategory;
import com.personal.grievanceManagement.entities.GrievanceStatus;
import com.personal.grievanceManagement.entities.Users;
import com.personal.grievanceManagement.repositories.GrievanceRepository;
import com.personal.grievanceManagement.repositories.UsersRepository;
import com.personal.grievanceManagement.security.CustomUserDetails;
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
    private UserService userService;

    public Grievance submitGrievance(String title, String description, CustomUserDetails userDetails) {
        Long userId = userService.getUserId(userDetails);
        Users user = userService.findByUserId(userId);
        Grievance grievance = new Grievance();
        grievance.setTitle(title);
        grievance.setDescription(description);
        grievance.setStatus(GrievanceStatus.PENDING);
        grievance.setSubmitted_on(LocalDateTime.now());
        grievance.setUser(user);
        return grievanceRepository.save(grievance);
    }
    public List<Grievance> getGrievancesByUserId(CustomUserDetails userDetails) {
        Long userId = userService.getUserId(userDetails);
        Users user = userService.findByUserId(userId);
        return grievanceRepository.findAllByUser(user);
    }
    public List<Grievance> getAllUnassignedGrievances() {
        return grievanceRepository.findByCategoryIsNull();
    }
    public Grievance assignGrievance(Long grievanceId, Long assigneeId, GrievanceCategory grievanceCategory) {
        Grievance grievance = grievanceRepository.findById(grievanceId)
                .orElseThrow(() -> new RuntimeException("Grievance not found."));
        Users assignee = usersRepository.findById(assigneeId)
                        .orElseThrow(() -> new RuntimeException("Assignee not found"));
        grievance.setCategory(grievanceCategory);
        grievance.setAssignee(assignee);
        return grievanceRepository.save(grievance);
    }
    public List<Grievance> getAllAssignedGrievances(){
        return grievanceRepository.findByCategoryIsNotNull();
    }
    public List<Grievance> getAllUnresolvedGrievances(){
        return grievanceRepository.findByCategoryIsNotNullAndStatusNot(GrievanceStatus.RESOLVED);
    }
    public List<Grievance> getAllGrievancesByAssigneeId(CustomUserDetails userDetails) {
        Long assigneeId = userService.getUserId(userDetails);
        Users assignee = usersRepository.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
        return grievanceRepository.findAllByAssignee(assignee);
    }
    public Grievance updateStatus(Long grievanceId, GrievanceStatus grievanceStatus) {
        Grievance grievance = grievanceRepository.findById(grievanceId)
                .orElseThrow(() -> new RuntimeException("Grievance not found"));
        grievance.setStatus(grievanceStatus);
        return grievanceRepository.save(grievance);
    }
}
