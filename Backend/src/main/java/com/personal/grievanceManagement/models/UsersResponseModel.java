package com.personal.grievanceManagement.models;

import com.personal.grievanceManagement.entities.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersResponseModel {
    private String message;
    private Users users;
    private String status;
}
