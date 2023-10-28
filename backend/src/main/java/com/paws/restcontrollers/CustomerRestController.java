package com.paws.restcontrollers;

import com.paws.exceptions.UsernameAlreadyExistsException;
import com.paws.services.customers.CustomerService;
import com.paws.services.customers.payloads.CustomerAuthenticationResult;
import com.paws.models.customers.AuthenticationResponse;
import com.paws.models.customers.LoginRequest;
import com.paws.models.customers.RegisterRequest;
import com.paws.services.customers.payloads.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        CustomerAuthenticationResult result = customerService.login(
                request.getUsername(),
                request.getPassword()
        );

        AuthenticationResponse response = mapToAuthResponse(result);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws UsernameAlreadyExistsException {
        CustomerAuthenticationResult result = customerService.register(request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getFullName(),
                request.getAddress(),
                request.getPhoneNumber(),
                request.getGender());

        return ResponseEntity.ok().body(mapToAuthResponse(result));
    }

    private AuthenticationResponse mapToAuthResponse(CustomerAuthenticationResult result) {
        CustomerDto customerDto = result.getCustomerDto();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .address(customerDto.getAddress())
                .email(customerDto.getEmail())
                .fullName(customerDto.getFullName())
                .gender(customerDto.getGender())
                .username(customerDto.getUsername())
                .phoneNumber(customerDto.getPhoneNumber())
                .id(customerDto.getId())
                .token(result.getToken()).build();

        return response;
    }
}
