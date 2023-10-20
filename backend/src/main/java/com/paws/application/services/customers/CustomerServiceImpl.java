package com.paws.application.services.customers;

import com.paws.application.common.errors.UsernameAlreadyExistsException;
import com.paws.application.services.customers.common.CustomerAuthenticationResult;
import com.paws.application.services.customers.common.CustomerDto;
import com.paws.application.services.jwts.JwtService;
import com.paws.persistence.common.enums.AppointmentLocation;
import com.paws.persistence.common.enums.Gender;
import com.paws.persistence.entities.Appointment;
import com.paws.persistence.entities.Customer;
import com.paws.persistence.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(AuthenticationManager authenticationManager, CustomerRepository customerRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerAuthenticationResult login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Customer customer = customerRepository.getCustomerByUsername(username);
        String token = jwtService.generateJwtToken(customer);

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

        String token = jwtService.generateJwtToken(customer);
        CustomerDto customerDto = mapToCustomerDto(customer);

        CustomerAuthenticationResult result = new CustomerAuthenticationResult();
        result.setCustomerDto(customerDto);
        result.setToken(token);

        return result;
    }

    @Override
    public Appointment makeAppointment(long customerId, AppointmentLocation location, LocalDateTime time, String note, int numPets) {
        return null;
    }

    @Override
    public void cancelAppointment(long customerId, long appointmentId) {

    }

    @Override
    public void updateProfile(long customerId, String email, String fullName, String address, String phoneNumber, Gender gender) {

    }

    @Override
    public void changePassword(long customerId, String oldPassword, String newPassword) {

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
