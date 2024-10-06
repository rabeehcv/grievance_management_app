package com.personal.grievanceManagement.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignGrievanceRequestModel {
    private Long grievanceId;
    private Long assigneeId;
    private String category;
}
