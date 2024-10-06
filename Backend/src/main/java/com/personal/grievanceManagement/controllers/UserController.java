package com.personal.grievanceManagement.controllers;

import com.personal.grievanceManagement.entities.Users;
import com.personal.grievanceManagement.models.*;
import com.personal.grievanceManagement.security.CustomUserDetails;
import com.personal.grievanceManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public UsersResponseModel addUser(@Validated @RequestBody UserRequestModel userRequestModel) {
        return userService.addUser(userRequestModel);
    }
    @GetMapping("/signIn")
    public LoginResponseModel signIn(Authentication authentication) {
        LoginResponseModel loginResponse = new LoginResponseModel();

        try {
            if (authentication != null && authentication.isAuthenticated()) {
                loginResponse.setMessage("Authenticated successfully!");
                loginResponse.setStatusCode(HttpStatus.OK.value());
            } else {
                loginResponse.setMessage("Authentication failed");
                loginResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            }
        } catch (Exception e) {
            loginResponse.setMessage("An error occurred during authentication: " + e.getMessage());
            loginResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return loginResponse;
    }
    @GetMapping("/accountPage")
    public ResponseEntity<UsersResponseModel> getUserData(Authentication authentication) {
        UsersResponseModel responseModel = new UsersResponseModel();
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userService.getUserId(userDetails);
            Users user = userService.findByUserId(userId);
            responseModel.setMessage("User data retrieved successfully");
            responseModel.setStatus("Success");
            responseModel.setUsers(user);
            return new ResponseEntity<>(responseModel,HttpStatus.OK);
        } catch (RuntimeException e) {
            responseModel.setMessage(e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.NOT_FOUND);
        }
    }
    
    @PatchMapping("/update/newPhone")
    public ResponseEntity<UsersResponseModel> updatePhone(Authentication authentication, @Validated @RequestBody UpdatePhoneRequestModel requestModel) {
        UsersResponseModel responseModel = new UsersResponseModel();
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Users updatedUser = userService.updatePhone(userDetails, requestModel.getNewPhone());
            responseModel.setMessage("Phone updated successfully");
            responseModel.setStatus("Success");
            responseModel.setUsers(updatedUser);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (RuntimeException e) {
            responseModel.setMessage(e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/update/newPassword")
    public ResponseEntity<UsersResponseModel> updatePassword(Authentication authentication, @Validated @RequestBody UpdatePasswordRequestModel requestModel) {
        UsersResponseModel responseModel = new UsersResponseModel();
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Users updatedUser = userService.updatePassword(userDetails, requestModel.getNewPassword());
            responseModel.setMessage("Password updated successfully");
            responseModel.setStatus("Success");
            responseModel.setUsers(updatedUser);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (RuntimeException e) {
            responseModel.setMessage(e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("/update/newEmail")
    public ResponseEntity<UsersResponseModel> updateEmail(Authentication authentication, @Validated @RequestBody UpdateEmailRequestModel requestModel) {
        UsersResponseModel responseModel = new UsersResponseModel();
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Users updatedUser = userService.updateEmail(userDetails, requestModel.getNewEmail());
            responseModel.setMessage("Email updated successfully");
            responseModel.setStatus("Success");
            responseModel.setUsers(updatedUser);
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (RuntimeException e) {
            responseModel.setMessage(e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/supervisor/allAssignees")
    public ResponseEntity<AssigneeResponseModel> getAssignees(Authentication authentication) {
        AssigneeResponseModel responseModel = new AssigneeResponseModel();
        try {
            List<Users> assignees = userService.getAssignees();
            responseModel.setAssignees(assignees);
            responseModel.setMessage("Assignees fetched successfully");
            responseModel.setStatus("Success");
            return new ResponseEntity<>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            responseModel.setMessage("Failed to fetch assignees: " + e.getMessage());
            responseModel.setStatus("Failure");
            return new ResponseEntity<>(responseModel, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
