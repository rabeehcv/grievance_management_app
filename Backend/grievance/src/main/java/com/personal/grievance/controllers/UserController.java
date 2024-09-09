package com.personal.grievance.controllers;

import com.personal.grievance.entities.Users;
import com.personal.grievance.models.UsersResponseModel;
import com.personal.grievance.repositories.UsersRepository;
import com.personal.grievance.security.CustomUserDetails;
import com.personal.grievance.services.UserService;
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

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("/create")
    public UsersResponseModel addUser(@Validated @RequestBody Users user) {
        return userService.addUser(user);
    }
    @GetMapping("/user/email/{email}/password/{password}")
    public ResponseEntity<Users> getUserByUserId(@PathVariable String email, @PathVariable String password) {
        try {
            String usernameWithRole = "USER"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = getUserId(userDetails);
            Users user = userService.getUserByUserId(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
    @PatchMapping("/user/email/{email}/password/{password}/newPhone/{newPhone}")
    public ResponseEntity<Users> updatePhone(@PathVariable String email, @PathVariable String password, @PathVariable String newPhone) {
        try{
            String usernameWithRole = "USER"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = getUserId(userDetails);
            return userService.updateUserPhone(userId, newPhone);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
    @PatchMapping("/user/email/{email}/password/{password}/newEmail/{newEmail}")
    public ResponseEntity<Users> updateEmail(@PathVariable String email, @PathVariable String password, @PathVariable String newEmail) {
        try{
            String usernameWithRole = "USER"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = getUserId(userDetails);
            return userService.updateUserEmail(userId, newEmail);
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(null);
        }
    }
    @PatchMapping("/user/email/{email}/password/{password}/newPassword/{newPassword}")
    public ResponseEntity<Users> updatePassword(@PathVariable String email, @PathVariable String password, @PathVariable String newPassword) {
        try{
            String usernameWithRole = "USER"+ ":" + email;
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usernameWithRole, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = getUserId(userDetails);
            return userService.updateUserPassword(userId, newPassword);
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
