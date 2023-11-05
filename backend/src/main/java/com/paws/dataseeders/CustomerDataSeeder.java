package com.paws.dataseeders;

import com.paws.entities.Customer;
import com.paws.entities.common.enums.Gender;
import com.paws.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataSeeder implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerDataSeeder(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(customerRepository.count() == 0) {
            Customer customer = new Customer();
            customer.setAddress("");
            customer.setEmail("");
            customer.setGender(Gender.MALE);
            customer.setFullName("");
            customer.setPhoneNumber("");
            customer.setUsername("admin");
            customer.setPassword(passwordEncoder.encode("admin"));

            customerRepository.save(customer);
        }
    }
}
