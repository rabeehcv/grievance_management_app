package com.personal.grievance.security;

import com.personal.grievance.entities.Assignee;
import com.personal.grievance.entities.Role;
import com.personal.grievance.entities.Supervisor;
import com.personal.grievance.entities.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private String email;
    private String password;
    private Role role;

    public CustomUserDetails(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static CustomUserDetails fromUserEntity(Users user) {
        return new CustomUserDetails(user.getEmail(), user.getPassword(), Role.USER);
    }
    public static CustomUserDetails fromSupervisorEntity(Supervisor supervisor) {
        return new CustomUserDetails(supervisor.getEmail(), supervisor.getPassword(), Role.SUPERVISOR);
    }
    public static CustomUserDetails fromAssigneeEntity(Assignee assignee) {
        return new CustomUserDetails(assignee.getEmail(), assignee.getPassword(), Role.ASSIGNEE);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
