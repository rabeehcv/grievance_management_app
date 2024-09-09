package com.personal.grievance.services;

import com.personal.grievance.entities.Assignee;
import com.personal.grievance.models.AssigneeResponseModel;
import com.personal.grievance.repositories.AssigneeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssigneeService {
    @Autowired
    private AssigneeRepository assigneeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AssigneeResponseModel addAssignee(Assignee assignee) {
        assignee.setPassword(passwordEncoder.encode(assignee.getPassword()));
        assigneeRepository.save(assignee);
        AssigneeResponseModel assigneeResponseModel = new AssigneeResponseModel();
        assigneeResponseModel.setMessage("New assignee added.");
        assigneeResponseModel.setAssignee(assignee);
        return assigneeResponseModel;
    }

    public List<Assignee> getAllAssignee() {
       return assigneeRepository.findAll();
    }
}
