package com.personal.grievanceManagement.controllers;

import com.personal.grievanceManagement.entities.Users;
import com.personal.grievanceManagement.models.UsersResponseModel;
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
    public UsersResponseModel addUser(@Validated @RequestBody Users user) {
        return userService.addUser(user);
    }
    @GetMapping("/signIn")
    public ResponseEntity<String> signIn(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Authenticated successfully!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }
    @GetMapping("/accountPage")
    public ResponseEntity<Users> getUserData(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userService.getUserId(userDetails);
        Users user = userService.findByUserId(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PatchMapping("/update/newPhone/{newPhone}")
    public ResponseEntity<Users> updatePhone(Authentication authentication, @PathVariable String newPhone) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new ResponseEntity<>(userService.updatePhone(userDetails, newPhone), HttpStatus.OK);
    }
    @PatchMapping("/update/newPassword/{newPassword}")
    public ResponseEntity<Users> updatePassword(Authentication authentication, @PathVariable String newPassword) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new ResponseEntity<>(userService.updatePassword(userDetails, newPassword), HttpStatus.OK);
    }
    @PatchMapping("/update/newEmail/{newEmail}")
    public ResponseEntity<Users> updateEmail(Authentication authentication, @PathVariable String newEmail) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new ResponseEntity<>(userService.updateEmail(userDetails, newEmail), HttpStatus.OK);
    }
    @GetMapping("/supervisor/allAssignees")
    public ResponseEntity<List<Users>> getAssignees(Authentication authentication) {
        return new ResponseEntity<>(userService.getAssignees(), HttpStatus.OK);
    }
}
