package com.paws.security;

import com.paws.entities.Employee;
import com.paws.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.getEmployeeByUsername(username);
        if(employee == null) {
            throw new UsernameNotFoundException("Username does not exist.");
        }

        List<SimpleGrantedAuthority> roles = employee.getRoles()
                .stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();

        UserDetails user = new User(
                employee.getUsername(),
                employee.getPassword(),
                roles);

        return user;
    }
}
