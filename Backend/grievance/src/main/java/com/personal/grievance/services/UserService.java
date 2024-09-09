package com.personal.grievance.services;

import com.personal.grievance.entities.Users;
import com.personal.grievance.models.UsersResponseModel;
import com.personal.grievance.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsersResponseModel addUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        UsersResponseModel usersResponseModel = new UsersResponseModel();
        usersResponseModel.setMessage("New user added.");
        usersResponseModel.setUsers(user);
        return usersResponseModel;
    }

    public Users getUserByUserId(Long userId) {
        Optional<Users> opt = usersRepository.findById(userId);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }

    public ResponseEntity<Users> updateUserPhone(Long userId, String phone) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhone(phone);
        usersRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<Users> updateUserEmail(Long userId, String email) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(email);
        usersRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    public ResponseEntity<Users> updateUserPassword(Long userId, String password) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(password));
        usersRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
