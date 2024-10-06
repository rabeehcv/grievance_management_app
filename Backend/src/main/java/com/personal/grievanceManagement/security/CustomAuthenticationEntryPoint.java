package com.personal.grievanceManagement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.grievanceManagement.models.LoginResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        LoginResponseModel loginResponse = new LoginResponseModel();
        loginResponse.setMessage("Authentication failed: " + authException.getMessage());
        loginResponse.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));
    }
}
