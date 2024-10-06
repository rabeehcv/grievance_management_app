package com.personal.grievanceManagement.controllers;

import com.personal.grievanceManagement.entities.Grievance;
import com.personal.grievanceManagement.entities.GrievanceCategory;
import com.personal.grievanceManagement.entities.GrievanceStatus;
import com.personal.grievanceManagement.models.AssignGrievanceRequestModel;
import com.personal.grievanceManagement.models.GrievanceResponseModel;
import com.personal.grievanceManagement.models.GrievanceSubmitRequestModel;
import com.personal.grievanceManagement.models.UpdateGrievanceStatusRequestModel;
import com.personal.grievanceManagement.security.CustomUserDetails;
import com.personal.grievanceManagement.services.GrievanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/grievance")
public class GrievanceController {
    @Autowired
    private GrievanceService grievanceService;

    @PostMapping("/user/submit")
    public ResponseEntity<GrievanceResponseModel> submitGrievance(
            @RequestBody GrievanceSubmitRequestModel requestModel,
            Authentication authentication) {

        GrievanceResponseModel responseModel = new GrievanceResponseModel();

        if (requestModel.getTitle() == null || requestModel.getTitle().trim().isEmpty()) {
            responseModel.setMessage("Title is required.");
            responseModel.setStatus("Failure");
            return ResponseEntity.badRequest().body(responseModel);
        }

        if (requestModel.getDescription() == null || requestModel.getDescription().trim().isEmpty()) {
            responseModel.setMessage("Description is required.");
            responseModel.setStatus("Failure");
            return ResponseEntity.badRequest().body(responseModel);
        }

        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Grievance grievance = grievanceService.submitGrievance(
                    requestModel.getTitle(), requestModel.getDescription(), userDetails);

            responseModel.setMessage("Grievance submitted successfully!");
            responseModel.setStatus("Success");
            responseModel.setData(Collections.singletonList(grievance));
            return ResponseEntity.ok(responseModel);
        } catch (Exception e) {
            responseModel.setMessage("Failed to submit grievance: " + e.getMessage());
            responseModel.setStatus("Failure");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }

    @GetMapping("/user/allGrievances")
    public ResponseEntity<GrievanceResponseModel> getGrievancesByUserId(Authentication authentication) {
        GrievanceResponseModel responseModel = new GrievanceResponseModel();
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            List<Grievance> grievances = grievanceService.getGrievancesByUserId(userDetails);
            responseModel.setMessage("Grievances retrieved successfully");
            responseModel.setStatus("Success");
            responseModel.setData(grievances);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (RuntimeException e) {
            responseModel.setMessage("Failed to retrieve grievances");
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/supervisor/unassignedGrievances")
    public ResponseEntity<GrievanceResponseModel> getAllUnassignedGrievances(Authentication authentication) {
        GrievanceResponseModel responseModel = new GrievanceResponseModel();
        try {
            List<Grievance> grievances = grievanceService.getAllUnassignedGrievances();
            if (grievances.isEmpty()) {
                responseModel.setMessage("No unassigned grievances found.");
                responseModel.setStatus("Success");
                responseModel.setData(grievances);  // Returning an empty list
                return new ResponseEntity<>(responseModel, HttpStatus.OK);
            }
            responseModel.setMessage("Unassigned grievances retrieved successfully.");
            responseModel.setStatus("Success");
            responseModel.setData(grievances);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            responseModel.setMessage("Failed to retrieve unassigned grievances: " + e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/supervisor/assignGrievance")
    public ResponseEntity<GrievanceResponseModel> assignGrievance(
            @RequestBody AssignGrievanceRequestModel requestModel,
            Authentication authentication) {

        GrievanceResponseModel responseModel = new GrievanceResponseModel();

        try {
            GrievanceCategory grievanceCategory = GrievanceCategory.valueOf(requestModel.getCategory());
            Grievance grievance = grievanceService.assignGrievance(
                    requestModel.getGrievanceId(), requestModel.getAssigneeId(), grievanceCategory);

            responseModel.setMessage("Grievance assigned successfully");
            responseModel.setStatus("Success");
            responseModel.setData(Collections.singletonList(grievance));
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            responseModel.setMessage("Invalid category: " + requestModel.getCategory());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            responseModel.setMessage("Failed to assign grievance: " + e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/supervisor/allAssignedGrievances")
    public ResponseEntity<GrievanceResponseModel> getAllAssignedGrievances(Authentication authentication) {
        GrievanceResponseModel responseModel = new GrievanceResponseModel();
        try {
            List<Grievance> grievances = grievanceService.getAllAssignedGrievances();
            responseModel.setMessage("Assigned grievances fetched successfully.");
            responseModel.setStatus("Success");
            responseModel.setData(grievances);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            responseModel.setMessage("Failed to fetch assigned grievances: " + e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/supervisor/unresolvedGrievance")
    public ResponseEntity<GrievanceResponseModel> getAllUnresolvedGrievances(Authentication authentication) {
        GrievanceResponseModel responseModel = new GrievanceResponseModel();

        try {
            List<Grievance> grievances = grievanceService.getAllUnresolvedGrievances();
            responseModel.setMessage("Unresolved grievances fetched successfully.");
            responseModel.setStatus("Success");
            responseModel.setData(grievances);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            responseModel.setMessage("Failed to fetch unresolved grievances: " + e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/assignee/assignedGrievances")
    public ResponseEntity<GrievanceResponseModel> getAllGrievancesByAssigneeId(Authentication authentication) {
        GrievanceResponseModel responseModel = new GrievanceResponseModel();
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            List<Grievance> grievances = grievanceService.getAllGrievancesByAssigneeId(userDetails);
            responseModel.setMessage("Assigned grievances fetched successfully.");
            responseModel.setStatus("Success");
            responseModel.setData(grievances);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            responseModel.setMessage("Failed to fetch grievances: " + e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/assignee/updateStatus")
    public ResponseEntity<GrievanceResponseModel> updateStatus(
            @RequestBody UpdateGrievanceStatusRequestModel requestModel,
            Authentication authentication) {

        GrievanceResponseModel responseModel = new GrievanceResponseModel();

        try {
            GrievanceStatus grievanceStatus = GrievanceStatus.valueOf(requestModel.getStatus());
            Grievance updatedGrievance = grievanceService.updateStatus(
                    requestModel.getGrievanceId(), grievanceStatus);

            responseModel.setMessage("Grievance status updated successfully.");
            responseModel.setStatus("Success");
            responseModel.setData(Collections.singletonList(updatedGrievance));
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            responseModel.setMessage("Invalid status: " + requestModel.getStatus());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            responseModel.setMessage("Failed to update grievance status: " + e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
