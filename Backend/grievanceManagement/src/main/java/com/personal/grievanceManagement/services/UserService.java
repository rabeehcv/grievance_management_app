package com.personal.grievanceManagement.services;

import com.personal.grievanceManagement.entities.Role;
import com.personal.grievanceManagement.entities.Users;
import com.personal.grievanceManagement.models.UsersResponseModel;
import com.personal.grievanceManagement.repositories.UsersRepository;
import com.personal.grievanceManagement.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersResponseModel addUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        usersRepository.save(user);
        UsersResponseModel usersResponseModel = new UsersResponseModel();
        usersResponseModel.setMessage("New user added.");
        usersResponseModel.setUsers(user);
        return usersResponseModel;
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
        Long userId = getUserId(userDetails);
        Users user = findByUserId(userId);
        user.setPhone(phone);
        return usersRepository.save(user);
    }
    public Users updatePassword(CustomUserDetails userDetails, String password){
        Long userId = getUserId(userDetails);
        Users user = findByUserId(userId);
        user.setPassword(passwordEncoder.encode(password));
        return usersRepository.save(user);
    }
    public Users updateEmail(CustomUserDetails userDetails, String email){
        Long userId = getUserId(userDetails);
        Users user = findByUserId(userId);
        user.setEmail(email);
        return usersRepository.save(user);
    }
    public List<Users> getAssignees() {
        return usersRepository.findByRole(Role.ASSIGNEE);
    }
}
