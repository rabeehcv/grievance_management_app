package com.personal.grievanceManagement.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmailRequestModel {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String newEmail;
}
