package com.personal.grievance.security;

import com.personal.grievance.entities.Assignee;
import com.personal.grievance.entities.Role;
import com.personal.grievance.entities.Supervisor;
import com.personal.grievance.entities.Users;
import com.personal.grievance.repositories.AssigneeRepository;
import com.personal.grievance.repositories.SupervisorRepository;
import com.personal.grievance.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SupervisorRepository supervisorRepository;

    @Autowired
    private AssigneeRepository assigneeRepository;

    @Override
    public UserDetails loadUserByUsername(String emailWithRole) throws UsernameNotFoundException {
        String[] parts = emailWithRole.split(":");
        if(parts.length != 2) {
            throw new UsernameNotFoundException("Invalid login format");
        }
        String rolePart = parts[0].toUpperCase();
        String email = parts[1];
        Role role;
        try {
            role = Role.valueOf(rolePart);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid Role");
        }
        switch (role) {
            case USER :
                Users user = usersRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return CustomUserDetails.fromUserEntity(user);
            case SUPERVISOR :
                Supervisor supervisor = supervisorRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Supervisor not found"));
                return CustomUserDetails.fromSupervisorEntity(supervisor);
            case ASSIGNEE :
                Assignee assignee = assigneeRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Assignee not found"));
                return CustomUserDetails.fromAssigneeEntity(assignee);
            default:
                throw new UsernameNotFoundException("Role not recognized");
        }
    }
}
