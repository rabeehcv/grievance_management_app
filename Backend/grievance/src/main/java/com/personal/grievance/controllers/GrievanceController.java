package com.personal.grievance.controllers;

import com.personal.grievance.entities.Grievance;
import com.personal.grievance.repositories.AssigneeRepository;
import com.personal.grievance.repositories.SupervisorRepository;
import com.personal.grievance.repositories.UsersRepository;
import com.personal.grievance.security.CustomUserDetails;
import com.personal.grievance.services.GrievanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    //@PreAuthorize("hasRole('USER')")
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
    private Long getUserId(CustomUserDetails userDetails) {
        return usersRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUserId();
    }
}
