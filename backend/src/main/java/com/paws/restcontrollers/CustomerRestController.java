package com.paws.restcontrollers;

import com.paws.exceptions.*;
import com.paws.models.customers.MakeAppointmentRequest;
import com.paws.payloads.response.CustomerDto;
import com.paws.services.appointments.AppointmentService;
import com.paws.services.customers.CustomerService;
import com.paws.payloads.response.CustomerAuthenticationResult;
import com.paws.models.customers.AuthenticationResponse;
import com.paws.models.customers.LoginRequest;
import com.paws.models.customers.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
@PreAuthorize("isAuthenticated()")
public class CustomerRestController extends BaseRestController{
    private final CustomerService customerService;
    private final AppointmentService appointmentService;

    @Autowired
    public CustomerRestController(CustomerService customerService, AppointmentService appointmentService) {
        this.customerService = customerService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        CustomerAuthenticationResult result = customerService.login(
                request.getUsername(),
                request.getPassword()
        );

        AuthenticationResponse response = mapToAuthResponse(result);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
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

    @GetMapping("")
    public ResponseEntity<?> getCustomerProfile(Principal principal) throws CustomerNotFoundException {
        CustomerDto customer = customerService.getProfile(principal.getName());

        return ResponseEntity.ok(customer);
    }

    @PostMapping("/appointments")
    public ResponseEntity<?> makeAppointment(@RequestBody @Valid MakeAppointmentRequest request, BindingResult bindingResult,Principal principal) throws PetTypeNotFoundException, InvalidAppointmentTimeException, SpaServiceNotFoundException, CustomerNotFoundException {
        if(bindingResult.hasErrors()) {
            return validationProblemDetails(bindingResult);
        }

        String username = principal.getName();

        appointmentService.makeAppointment(username,
                request.getLocation(),
                request.getTime(),
                request.getNote(),
                request.getAppointmentItems());

        return ResponseEntity.ok().body(null);
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
