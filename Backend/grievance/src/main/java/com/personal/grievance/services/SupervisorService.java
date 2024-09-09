package com.personal.grievance.services;

import com.personal.grievance.entities.Supervisor;
import com.personal.grievance.models.SupervisorResponseModel;
import com.personal.grievance.repositories.SupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SupervisorService {
    @Autowired
    SupervisorRepository supervisorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

   public SupervisorResponseModel addSupervisor(Supervisor supervisor) {
        supervisor.setPassword(passwordEncoder.encode(supervisor.getPassword()));
        supervisorRepository.save(supervisor);
        SupervisorResponseModel supervisorResponseModel = new SupervisorResponseModel();
        supervisorResponseModel.setMessage("New supervisor added successfully.");
        supervisorResponseModel.setSupervisor(supervisor);
        return supervisorResponseModel;
   }
}
