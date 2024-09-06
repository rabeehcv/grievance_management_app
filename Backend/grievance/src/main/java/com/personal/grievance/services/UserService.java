package com.personal.grievance.services;

import com.personal.grievance.entities.Users;
import com.personal.grievance.models.UsersResponseModel;
import com.personal.grievance.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}
