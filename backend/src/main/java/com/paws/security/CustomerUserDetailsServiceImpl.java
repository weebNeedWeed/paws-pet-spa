package com.paws.security;

import com.paws.entities.Customer;
import com.paws.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomerUserDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerUserDetailsServiceImpl(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.getCustomerByUsername(username);
        if(customer == null) {
            throw new UsernameNotFoundException("Username does not exist.");
        }

        User user = new User(
                customer.getUsername(),
                customer.getPassword(),
                new ArrayList<>());

        return user;
    }
}
