package com.personal.grievance.controllers;

import com.personal.grievance.entities.Grievance;
import com.personal.grievance.entities.Supervisor;
import com.personal.grievance.models.SupervisorResponseModel;
import com.personal.grievance.services.GrievanceService;
import com.personal.grievance.services.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {
    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GrievanceService grievanceService;

    @PostMapping("/create")
    public SupervisorResponseModel addSupervisor(@Validated @RequestBody Supervisor supervisor) {
        return supervisorService.addSupervisor(supervisor);
    }
    @GetMapping("/email/{email}/password/{password}/unassignedGrievances")
    public ResponseEntity<List<Grievance>> getAllUnassignedGrievances(@PathVariable String email, @PathVariable String password) {
        try {
            String usernameWithRole = "SUPERVISOR"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            List<Grievance> grievances = grievanceService.getAllUnassignedGrievances();
            if(grievances.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(grievances,HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
    @GetMapping("/email/{email}/password/{password}/allAssignedGrievances")
    public ResponseEntity<List<Grievance>> getAllAssignedGrievances(@PathVariable String email, @PathVariable String password){
        try {
            String usernameWithRole = "SUPERVISOR"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            List<Grievance> grievances = grievanceService.getAllAssignedGrievances();
            if(grievances.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(grievances,HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
    @GetMapping("/email/{email}/password/{password}/allUnresolvedGrievances")
    public ResponseEntity<List<Grievance>> getAllUnresolvedGrievances(@PathVariable String email, @PathVariable String password){
        try {
            String usernameWithRole = "SUPERVISOR"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            List<Grievance> grievances = grievanceService.getAllUnresolvedGrievances();
            if(grievances.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(grievances,HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
}
