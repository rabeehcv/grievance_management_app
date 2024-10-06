package com.personal.grievanceManagement.services;

import com.personal.grievanceManagement.entities.Role;
import com.personal.grievanceManagement.entities.Users;
import com.personal.grievanceManagement.models.UserRequestModel;
import com.personal.grievanceManagement.models.UsersResponseModel;
import com.personal.grievanceManagement.repositories.UsersRepository;
import com.personal.grievanceManagement.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersResponseModel addUser(UserRequestModel userRequestModel) {
        UsersResponseModel response = new UsersResponseModel();

        try {
            if (usersRepository.findByEmail(userRequestModel.getEmail()).isPresent()) {
                response.setMessage("Email already in use.");
                response.setStatus("Failure");
                return response;
            }
            Users user = new Users();
            user.setFirstName(userRequestModel.getFirstName());
            user.setLastName(userRequestModel.getLastName());
            user.setEmail(userRequestModel.getEmail());
            user.setPassword(passwordEncoder.encode(userRequestModel.getPassword()));
            user.setPhone(userRequestModel.getPhone());
            user.setRole(Role.USER);
            usersRepository.save(user);
            response.setMessage("New user added successfully.");
            response.setUsers(user);
            response.setStatus("Success");
        } catch (DataIntegrityViolationException ex) {
            response.setMessage("Error: Could not save user. Details: " + ex.getMessage());
        } catch (Exception ex) {
            response.setMessage("An unexpected error occurred. Details: " + ex.getMessage());
        }
        return response;
    }

    public Long getUserId(CustomUserDetails userDetails) {
        return usersRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUserId();
    }
    public Users findByUserId(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public Users updatePhone(CustomUserDetails userDetails, String phone){
        try {
            Long userId = getUserId(userDetails);
            Users user = findByUserId(userId);
            user.setPhone(phone);
            return usersRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update phone number");
        }
    }
    public Users updatePassword(CustomUserDetails userDetails, String password){
        try {
            Long userId = getUserId(userDetails);
            Users user = findByUserId(userId);
            user.setPassword(passwordEncoder.encode(password));
            return usersRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password");
        }
    }
    public Users updateEmail(CustomUserDetails userDetails, String email){
        try {
            Long userId = getUserId(userDetails);
            Users user = findByUserId(userId);
            user.setEmail(email);
            return usersRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update email");
        }
    }
    public List<Users> getAssignees() {
        try {
            return usersRepository.findByRole(Role.ASSIGNEE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch assignees: " + e.getMessage());
        }
    }
}
