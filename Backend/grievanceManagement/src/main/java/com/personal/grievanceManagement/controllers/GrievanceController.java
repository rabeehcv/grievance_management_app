package com.personal.grievanceManagement.controllers;

import com.personal.grievanceManagement.entities.Grievance;
import com.personal.grievanceManagement.entities.GrievanceCategory;
import com.personal.grievanceManagement.entities.GrievanceStatus;
import com.personal.grievanceManagement.security.CustomUserDetails;
import com.personal.grievanceManagement.services.GrievanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grievance")
public class GrievanceController {
    @Autowired
    private GrievanceService grievanceService;

    @PostMapping("/user/submit")
    public ResponseEntity<String> submitGrievance(@RequestParam String title, @RequestParam String description, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        grievanceService.submitGrievance(title, description, userDetails);
        return ResponseEntity.ok("Grievance submitted successfully!");
    }
    @GetMapping("/user/allGrievances")
    public ResponseEntity<List<Grievance>> getGrievancesByUserId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new ResponseEntity<>(grievanceService.getGrievancesByUserId(userDetails), HttpStatus.OK );
    }
    @GetMapping("/supervisor/unassignedGrievances")
    ResponseEntity<List<Grievance>> getAllUnassignedGrievances(Authentication authentication) {
        return new ResponseEntity<>(grievanceService.getAllUnassignedGrievances(), HttpStatus.OK );
    }
    @PatchMapping("/supervisor/assignGrievance/grievanceId/{grievanceId}/assigneeId/{assigneeId}/category/{category}")
    ResponseEntity<Grievance> assignGrievance(Authentication authentication, @PathVariable Long grievanceId, @PathVariable Long assigneeId, @PathVariable String category) {
        GrievanceCategory grievanceCategory;
        try {
            grievanceCategory = GrievanceCategory.valueOf(category);
        }catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid Category");
        }
        return new ResponseEntity<>(grievanceService.assignGrievance(grievanceId, assigneeId, grievanceCategory), HttpStatus.OK);
    }
    @GetMapping("/supervisor/allAssignedGrievances")
    ResponseEntity<List<Grievance>> getAllAssignedGrievances(Authentication authentication) {
        return new ResponseEntity<>(grievanceService.getAllAssignedGrievances(), HttpStatus.OK );
    }
    @GetMapping("/supervisor/unresolvedGrievance")
    ResponseEntity<List<Grievance>> getAllUnresolvedGrievances(Authentication authentication) {
        return new ResponseEntity<>(grievanceService.getAllUnresolvedGrievances(), HttpStatus.OK );
    }
    @GetMapping("/assignee/assignedGrievances")
    ResponseEntity<List<Grievance>> getAllGrievancesByAssigneeId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new ResponseEntity<>(grievanceService.getAllGrievancesByAssigneeId(userDetails), HttpStatus.OK );
    }
    @PatchMapping("/assignee/grievanceId/{grievanceId}/updateStatus/{status}")
    ResponseEntity<Grievance> updateStatus(Authentication authentication, @PathVariable Long grievanceId ,@PathVariable String status ) {
        GrievanceStatus grievanceStatus;
        try{
            grievanceStatus = GrievanceStatus.valueOf(status);
        }catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid Status");
        }
        return new ResponseEntity<>(grievanceService.updateStatus(grievanceId, grievanceStatus), HttpStatus.OK );
    }
}
