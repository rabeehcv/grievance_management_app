package com.personal.grievance.models;

import com.personal.grievance.entities.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersResponseModel {
    private String message;
    private Users users;
}
