package com.paws.persistence.dataseeders;

import com.paws.persistence.common.RoleNameConstants;
import com.paws.persistence.common.enums.Gender;
import com.paws.persistence.entities.Employee;
import com.paws.persistence.entities.Role;
import com.paws.persistence.repositories.EmployeeRepository;
import com.paws.persistence.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmployeeDataSeeder implements CommandLineRunner {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
            Employee admin = new Employee();

            admin.setAddress("");
            admin.setEmail("");
            admin.setDateOfBirth(LocalDateTime.now().toLocalDate());
            admin.setGender(Gender.MALE);
            admin.setFullName("");
            admin.setFirstWorkingDay(LocalDateTime.now().toLocalDate());
            admin.setIdentityNumber("");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setPhoneNumber("");

            Role adminRole = roleRepository.findFirstByNameEquals(RoleNameConstants.ADMINISTRATOR);
            adminRole.addEmployee(admin);

            roleRepository.save(adminRole);
        }
    }
}
