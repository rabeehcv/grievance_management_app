package com.personal.grievance.models;

import com.personal.grievance.entities.Assignee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssigneeResponseModel {
    private String message;
    private Assignee assignee;
}
