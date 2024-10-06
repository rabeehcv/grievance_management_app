package com.personal.grievanceManagement.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGrievanceStatusRequestModel {
    private Long grievanceId;
    private String status;
}
