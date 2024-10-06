package com.personal.grievanceManagement.models;

import com.personal.grievanceManagement.entities.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssigneeResponseModel {
    private String message;
    private List<Users> assignees;
    private String status;
}
