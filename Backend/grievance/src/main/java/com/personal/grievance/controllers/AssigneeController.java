package com.personal.grievance.controllers;

import com.personal.grievance.entities.Assignee;
import com.personal.grievance.entities.Grievance;
import com.personal.grievance.entities.GrievanceStatus;
import com.personal.grievance.models.AssigneeResponseModel;
import com.personal.grievance.repositories.AssigneeRepository;
import com.personal.grievance.security.CustomUserDetails;
import com.personal.grievance.services.AssigneeService;
import com.personal.grievance.services.GrievanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/assignee")
public class AssigneeController {
    @Autowired
    private AssigneeService assigneeService;

    @Autowired
    private GrievanceService grievanceService;

    @Autowired
    private AssigneeRepository assigneeRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/create")
    public AssigneeResponseModel addAssignee(@Validated @RequestBody Assignee assignee) {
        return assigneeService.addAssignee(assignee);
    }
    @GetMapping("/email/{email}/password/{password}/allAssignee")
    public ResponseEntity<List<Assignee>> getAllAssignee(@PathVariable String email, @PathVariable String password) {
        try {
            String usernameWithRole = "SUPERVISOR"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            List<Assignee> assignees = assigneeService.getAllAssignee();
            if(assignees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(assignees,HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
    @GetMapping("/email/{email}/password/{password}")
    public ResponseEntity<List<Grievance>> getGrievancesByAssigneeId(@PathVariable String email, @PathVariable String password) {
        try {
            String usernameWithRole = "ASSIGNEE"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = getUserId(userDetails);
            List<Grievance> grievances = grievanceService.getAllGrievancesByAssigneeId(userId);
            if(grievances.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(grievances,HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Collections.emptyList());
        }
    }
    @PatchMapping("/email/{email}/password/{password}/grievanceId/{grievanceId}/status/{status}")
    public ResponseEntity<Grievance> updateGrievanceStatus(@PathVariable String email, @PathVariable String password, @PathVariable Long grievanceId ,@PathVariable String status){
        try {
            String usernameWithRole = "ASSIGNEE"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            GrievanceStatus grievanceStatus;
            try{
                grievanceStatus = GrievanceStatus.valueOf(status);
            }catch (IllegalArgumentException e) {
                throw new UsernameNotFoundException("Invalid Status");
            }
            Grievance grievance = grievanceService.updateGrievanceStatus(grievanceId, grievanceStatus);
            return new ResponseEntity<>(grievance, HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    private Long getUserId(CustomUserDetails userDetails) {
        return assigneeRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getAssignee_id();
    }
}
