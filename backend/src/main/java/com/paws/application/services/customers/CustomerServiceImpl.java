package com.paws.application.services.customers;

import com.paws.application.services.customers.common.CustomerAuthenticationResult;
import com.paws.application.services.customers.common.CustomerDto;
import com.paws.application.services.jwts.JwtService;
import com.paws.persistence.common.Gender;
import com.paws.persistence.entities.Customer;
import com.paws.persistence.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    @Autowired
    public CustomerServiceImpl(AuthenticationManager authenticationManager, CustomerRepository customerRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.jwtService = jwtService;
    }

    @Override
    public CustomerAuthenticationResult login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = customerRepository.findFirstByUsernameEquals(username);
        String token = jwtService.generateJwtToken(customer);

        CustomerDto customerDto = mapToCustomerDto(customer);

        CustomerAuthenticationResult result = new CustomerAuthenticationResult();
        result.setCustomerDto(customerDto);
        result.setToken(token);

        return result;
    }

    @Override
    public CustomerAuthenticationResult register(String username, String password, String email, String fullName, String address, String phoneNumber, Gender gender) {
        return null;
    }

    private CustomerDto mapToCustomerDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setAddress(customer.getAddress());
        customerDto.setEmail(customer.getEmail());
        customerDto.setGender(customer.getGender());
        customerDto.setPhoneNumber(customer.getPhoneNumber());
        customerDto.setUsername(customer.getUsername());
        customerDto.setFullName(customer.getFullName());
        customerDto.setId(customer.getId());

        return customerDto;
    }
}
