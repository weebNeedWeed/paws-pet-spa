package com.paws.services.customers;

import com.paws.entities.*;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.exceptions.*;
import com.paws.repositories.AppointmentRepository;
import com.paws.repositories.PetTypeRepository;
import com.paws.repositories.SpaServiceRepository;
import com.paws.services.customers.payloads.CustomerAuthenticationResult;
import com.paws.services.customers.payloads.CustomerDto;
import com.paws.services.customers.payloads.MakeAppointmentItemRequest;
import com.paws.services.jwts.JwtService;
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
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;
    private final SpaServiceRepository spaServiceRepository;

    @Autowired
    public CustomerServiceImpl(AuthenticationManager authenticationManager, CustomerRepository customerRepository, PetTypeRepository petTypeRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AppointmentRepository appointmentRepository, SpaServiceRepository spaServiceRepository) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.petTypeRepository = petTypeRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.appointmentRepository = appointmentRepository;
        this.spaServiceRepository = spaServiceRepository;
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
    @Transactional
    public void makeAppointment(long customerId,
                                AppointmentLocation location,
                                LocalDateTime time,
                                String note,
                                List<MakeAppointmentItemRequest> makeAppointmentItemRequests) throws CustomerNotFoundException, InvalidAppointmentTimeException, PetTypeNotFoundException, SpaServiceNotFoundException {
        Customer customer = customerRepository.findById(customerId);
        if(customer == null) {
            throw new CustomerNotFoundException();
        }

        // Validate appointment time is valid
        LocalDateTime lowBoundary = LocalDateTime.now();
        LocalDateTime highBoundary = lowBoundary.plusDays(7);

        if(time.isBefore(lowBoundary) || time.isAfter(highBoundary)) {
            throw new InvalidAppointmentTimeException();
        }

        Appointment appointment = new Appointment();

        for(MakeAppointmentItemRequest request : makeAppointmentItemRequests) {
            PetType petType = petTypeRepository.findById(request.getPetTypeId());
            if(petType == null) {
                throw new PetTypeNotFoundException();
            }

            AppointmentItem appointmentItem = new AppointmentItem();
            appointmentItem.setPetName(request.getPetName());
            appointmentItem.setPetType(petType);

            for(Long serviceId : request.getServiceIds()) {
                SpaService spaService = spaServiceRepository.getSpaServiceById(serviceId);
                if(spaService == null) {
                    throw new SpaServiceNotFoundException();
                }

                appointmentItem.getSpaServices().add(spaService);
            }

            appointment.addItem(appointmentItem);
        }

        // TODO: set it to pending later
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNote(note);
        appointment.setAppointmentTime(time);
        appointment.setLocation(location);

        customer.addAppointment(appointment);

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void cancelAppointment(long customerId, long appointmentId) throws CustomerNotFoundException, AppointmentNotFoundException {
        Customer customer = customerRepository.findById(customerId);
        if(customer == null) {
            throw new CustomerNotFoundException();
        }

        Appointment appointment = appointmentRepository.findByAppointmentIdAndCustomerId(appointmentId, customerId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(appointment);
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
