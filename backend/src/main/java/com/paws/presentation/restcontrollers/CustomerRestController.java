package com.paws.presentation.restcontrollers;

import com.paws.application.common.errors.UsernameAlreadyExistsException;
import com.paws.application.services.customers.CustomerService;
import com.paws.application.services.customers.common.CustomerAuthenticationResult;
import com.paws.presentation.restcontrollers.contracts.customers.AuthenticationResponse;
import com.paws.presentation.restcontrollers.contracts.customers.LoginRequest;
import com.paws.presentation.restcontrollers.contracts.customers.RegisterRequest;
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
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAddress(result.getCustomerDto().getAddress());
        response.setEmail(result.getCustomerDto().getEmail());
        response.setFullName(result.getCustomerDto().getFullName());
        response.setGender(result.getCustomerDto().getGender());
        response.setUsername(result.getCustomerDto().getUsername());
        response.setPhoneNumber(result.getCustomerDto().getPhoneNumber());
        response.setId(result.getCustomerDto().getId());
        response.setToken(result.getToken());

        return response;
    }
}
