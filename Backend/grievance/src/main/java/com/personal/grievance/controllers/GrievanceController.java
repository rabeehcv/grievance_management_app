package com.personal.grievance.controllers;

import com.personal.grievance.entities.Grievance;
import com.personal.grievance.entities.GrievanceCategory;
import com.personal.grievance.repositories.AssigneeRepository;
import com.personal.grievance.repositories.SupervisorRepository;
import com.personal.grievance.repositories.UsersRepository;
import com.personal.grievance.security.CustomUserDetails;
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
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/grievances")
public class GrievanceController {
    @Autowired
    private GrievanceService grievanceService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private SupervisorRepository supervisorRepository;
    @Autowired
    private AssigneeRepository assigneeRepository;
    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/submit")
    public ResponseEntity<String> submitGrievance(@RequestParam String title, @RequestParam String description,  @RequestParam String email, @RequestParam String password) {
            try {
                String usernameWithRole = "USER"+ ":" + email;
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(usernameWithRole, password)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                Long userId = getUserId(userDetails);
                Grievance grievance = grievanceService.createGrievance(title, description, userId);
                return ResponseEntity.ok("Grievance submitted successfully.");
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(401).body("Invalid Credentials.");
            }
    }
    @GetMapping("/user/email/{email}/password/{password}")
    public ResponseEntity<List<Grievance>> getGrievancesByUserId( @PathVariable String email, @PathVariable String password) {
        try {
            String usernameWithRole = "USER"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = getUserId(userDetails);
            List<Grievance> grievances = grievanceService.getAllGrievancesByUserId(userId);
            if(grievances.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(grievances,HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Collections.emptyList());
        }
    }
    @PatchMapping("/supervisor/email/{email}/password/{password}/grievanceId/{grievanceId}/assigneeId/{assigneeId}/category/{category}")
    public ResponseEntity<Grievance> assignGrievance(@PathVariable String email, @PathVariable String password, @PathVariable Long grievanceId, @PathVariable Long assigneeId, @PathVariable String category) {
        try{
            String usernameWithRole = "SUPERVISOR"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            GrievanceCategory grievanceCategory;
            try {
                grievanceCategory = GrievanceCategory.valueOf(category);
            }catch (IllegalArgumentException e) {
                throw new UsernameNotFoundException("Invalid Category");
            }
            Grievance grievance = grievanceService.assignGrievance(grievanceId, assigneeId, grievanceCategory);
            return new ResponseEntity<>(grievance, HttpStatus.OK);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
    private Long getUserId(CustomUserDetails userDetails) {
        return usersRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUserId();
    }
}
