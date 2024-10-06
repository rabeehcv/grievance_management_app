package com.personal.grievanceManagement.services;

import com.personal.grievanceManagement.entities.*;
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
        try {
            Long userId = userService.getUserId(userDetails);
            Users user = userService.findByUserId(userId);
            Grievance grievance = new Grievance();
            grievance.setTitle(title);
            grievance.setDescription(description);
            grievance.setStatus(GrievanceStatus.PENDING);
            grievance.setSubmitted_on(LocalDateTime.now());
            grievance.setUser(user);
            return grievanceRepository.save(grievance);
        } catch (Exception e) {
            throw new RuntimeException("Error while submitting grievance: " + e.getMessage());
        }
    }
    public List<Grievance> getGrievancesByUserId(CustomUserDetails userDetails) {
        try {
            Long userId = userService.getUserId(userDetails);
            Users user = userService.findByUserId(userId);
            return grievanceRepository.findAllByUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve grievances: " + e.getMessage());
        }
    }
    public List<Grievance> getAllUnassignedGrievances() {
        try {
            return grievanceRepository.findByCategoryIsNull();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch unassigned grievances: " + e.getMessage());
        }
    }
    public Grievance assignGrievance(Long grievanceId, Long assigneeId, GrievanceCategory grievanceCategory) {
        try {
            Grievance grievance = grievanceRepository.findById(grievanceId)
                    .orElseThrow(() -> new RuntimeException("Grievance not found."));
            Users assignee = usersRepository.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found."));
            if (!assignee.getRole().equals(Role.ASSIGNEE)) {
                throw new RuntimeException("Invalid assignee ID: User is not an ASSIGNEE.");
            }
            grievance.setCategory(grievanceCategory);
            grievance.setAssignee(assignee);
            return grievanceRepository.save(grievance);
        } catch (Exception e) {
            throw new RuntimeException("Error while assigning grievance: " + e.getMessage());
        }
    }
    public List<Grievance> getAllAssignedGrievances(){
        try {
            return grievanceRepository.findByCategoryIsNotNull();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching assigned grievances: " + e.getMessage());
        }
    }
    public List<Grievance> getAllUnresolvedGrievances(){
        try {
            return grievanceRepository.findByCategoryIsNotNullAndStatusNot(GrievanceStatus.RESOLVED);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching unresolved grievances: " + e.getMessage());
        }
    }
    public List<Grievance> getAllGrievancesByAssigneeId(CustomUserDetails userDetails) {
        try {
            Long assigneeId = userService.getUserId(userDetails);
            Users assignee = usersRepository.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            return grievanceRepository.findAllByAssignee(assignee);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching grievances: " + e.getMessage());
        }
    }
    public Grievance updateStatus(Long grievanceId, GrievanceStatus grievanceStatus) {
        try {
            Grievance grievance = grievanceRepository.findById(grievanceId)
                    .orElseThrow(() -> new RuntimeException("Grievance not found"));
            grievance.setStatus(grievanceStatus);
            return grievanceRepository.save(grievance);
        } catch (Exception e) {
            throw new RuntimeException("Error updating grievance status: " + e.getMessage());
        }
    }
}
