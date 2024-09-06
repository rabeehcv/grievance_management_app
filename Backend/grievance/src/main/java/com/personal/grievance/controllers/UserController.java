package com.personal.grievance.controllers;

import com.personal.grievance.entities.Users;
import com.personal.grievance.models.UsersResponseModel;
import com.personal.grievance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public UsersResponseModel addUser(@Validated @RequestBody Users user) {
        return userService.addUser(user);
    }
}
