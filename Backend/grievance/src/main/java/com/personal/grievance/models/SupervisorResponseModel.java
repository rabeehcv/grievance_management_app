package com.personal.grievance.models;

import com.personal.grievance.entities.Supervisor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupervisorResponseModel {
    private String message;
    private Supervisor supervisor;
}
