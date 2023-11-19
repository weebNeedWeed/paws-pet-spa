package com.paws.dataseeders;

import com.paws.entities.common.RoleNameConstants;
import com.paws.entities.common.enums.Gender;
import com.paws.entities.Employee;
import com.paws.entities.Role;
import com.paws.repositories.EmployeeRepository;
import com.paws.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EmployeeDataSeeder implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeDataSeeder(EmployeeRepository employeeRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        seedRoleData();
        seedAdministratorAccount();
    }

    private void seedRoleData() {
        if(roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName(RoleNameConstants.ADMINISTRATOR);

            Role coordinatorRole = new Role();
            coordinatorRole.setName(RoleNameConstants.COORDINATOR);

            Role normalEmpRole = new Role();
            normalEmpRole.setName(RoleNameConstants.NORMAL_EMPLOYEE);

            roleRepository.save(adminRole);
            roleRepository.save(coordinatorRole);
            roleRepository.save(normalEmpRole);
        }
    }

    private void seedAdministratorAccount() {
        if(employeeRepository.count() == 0) {
            seedEmployee("Nguyen van A", "emp1", RoleNameConstants.NORMAL_EMPLOYEE);
            seedEmployee("Le van B", "emp2", RoleNameConstants.NORMAL_EMPLOYEE);

            seedEmployee("Admin", "admin", RoleNameConstants.ADMINISTRATOR);

            seedEmployee("Coordinator", "coordinator", RoleNameConstants.COORDINATOR);
        }
    }

    private void seedEmployee(String fullName, String username, String roleName) {
        Employee emp1 = new Employee();

        emp1.setAddress("");
        emp1.setEmail(username + "@gmail.com");
        emp1.setDateOfBirth(LocalDateTime.now().toLocalDate());
        emp1.setGender(Gender.MALE);
        emp1.setFullName(fullName);
        emp1.setFirstWorkingDay(LocalDateTime.now().toLocalDate());
        emp1.setIdentityNumber(Stream.of(username.getBytes()).map(Object::toString).collect(Collectors.joining("")));
        emp1.setUsername(username);
        emp1.setPassword(passwordEncoder.encode(username));
        emp1.setPhoneNumber("012345678910");

        Role role = roleRepository.findFirstByNameEquals(roleName);
        role.addEmployee(emp1);

        roleRepository.save(role);
    }
}
