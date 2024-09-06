package com.personal.grievance.services;

import com.personal.grievance.entities.Grievance;
import com.personal.grievance.entities.GrievanceStatus;
import com.personal.grievance.entities.Users;
import com.personal.grievance.repositories.GrievanceRepository;
import com.personal.grievance.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GrievanceService {
    @Autowired
    GrievanceRepository grievanceRepository;

    @Autowired
    UsersRepository usersRepository;

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
}
