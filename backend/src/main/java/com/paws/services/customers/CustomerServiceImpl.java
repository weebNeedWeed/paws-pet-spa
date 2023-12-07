package com.paws.services.customers;

import com.paws.entities.*;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.exceptions.*;
import com.paws.repositories.AppointmentRepository;
import com.paws.repositories.PetTypeRepository;
import com.paws.repositories.SpaServiceRepository;
import com.paws.payloads.response.CustomerAuthenticationResult;
import com.paws.payloads.response.CustomerDto;
import com.paws.payloads.request.MakeAppointmentItemRequest;
import com.paws.security.JwtUtilities;
import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.entities.common.enums.Gender;
import com.paws.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final PetTypeRepository petTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;
    private final SpaServiceRepository spaServiceRepository;
    private final JwtUtilities jwtUtilities;

    @Autowired
    public CustomerServiceImpl(AuthenticationManager authenticationManager, CustomerRepository customerRepository, PetTypeRepository petTypeRepository, PasswordEncoder passwordEncoder, AppointmentRepository appointmentRepository, SpaServiceRepository spaServiceRepository, JwtUtilities jwtUtilities) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.petTypeRepository = petTypeRepository;
        this.passwordEncoder = passwordEncoder;
        this.appointmentRepository = appointmentRepository;
        this.spaServiceRepository = spaServiceRepository;
        this.jwtUtilities = jwtUtilities;
    }

    @Override
    public CustomerAuthenticationResult login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = customerRepository.getCustomerByUsername(username);
        String token = jwtUtilities.generateJwtToken(customer);

        CustomerDto customerDto = mapToCustomerDto(customer);

        CustomerAuthenticationResult result = new CustomerAuthenticationResult();
        result.setCustomerDto(customerDto);
        result.setToken(token);

        return result;
    }

    @Override
    public CustomerAuthenticationResult register(String username,
                                                 String password,
                                                 String email,
                                                 String fullName,
                                                 String address,
                                                 String phoneNumber,
                                                 Gender gender) throws UsernameAlreadyExistsException {
        if(customerRepository.existsCustomerWithUsername(username)) {
            throw new UsernameAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(password);

        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setAddress(address);
        customer.setEmail(email);
        customer.setPassword(encodedPassword);
        customer.setPhoneNumber(phoneNumber);
        customer.setGender(gender);
        customer.setFullName(fullName);

        customerRepository.save(customer);

        String token = jwtUtilities.generateJwtToken(customer);
        CustomerDto customerDto = mapToCustomerDto(customer);

        CustomerAuthenticationResult result = new CustomerAuthenticationResult();
        result.setCustomerDto(customerDto);
        result.setToken(token);

        return result;
    }

    @Override
    public CustomerDto getProfile(String username) throws CustomerNotFoundException {
        Customer customer = customerRepository.getCustomerByUsername(username);
        if(customer == null) {
            throw new CustomerNotFoundException();
        }

        return mapToCustomerDto(customer);
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
