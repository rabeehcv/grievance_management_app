package com.personal.grievanceManagement.models;

import com.personal.grievanceManagement.entities.Grievance;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GrievanceResponseModel {
    private String message;
    private String status;
    private List<Grievance> data;
}
