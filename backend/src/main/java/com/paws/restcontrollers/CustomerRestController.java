package com.paws.restcontrollers;

import com.paws.exceptions.UsernameAlreadyExistsException;
import com.paws.services.customers.CustomerService;
import com.paws.payloads.response.CustomerAuthenticationResult;
import com.paws.models.customers.AuthenticationResponse;
import com.paws.models.customers.LoginRequest;
import com.paws.models.customers.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController extends BaseRestController{
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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) throws UsernameAlreadyExistsException {
        if(bindingResult.hasErrors()) {
            return validationProblemDetails(bindingResult);
        }

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
