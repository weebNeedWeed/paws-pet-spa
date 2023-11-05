package com.paws.dataseeders;

import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.models.spaservices.CreateSpaServiceRequest;
import com.paws.services.customers.CustomerService;
import com.paws.services.customers.payloads.MakeAppointmentItemRequest;
import com.paws.services.employees.EmployeeService;
import com.paws.services.spasvcs.SpaSvcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestCmdLineRunner implements CommandLineRunner {
    @Autowired
    private SpaSvcService spaSvcService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public void run(String... args) throws Exception {
//        List<MakeAppointmentItemRequest> requests = new ArrayList<>();
//        MakeAppointmentItemRequest request = new MakeAppointmentItemRequest();
//        request.setPetName("some name");
//        request.setPetTypeId(1);
//        List<Long> l = new ArrayList<>();
//        l.add(1L);
//        request.setServiceIds(l);
//        request.getServiceIds().add(1L);
//        requests.add(request);
//
//        customerService.makeAppointment(
//                1,
//                AppointmentLocation.AT_SHOP,
//                LocalDateTime.now().plusMinutes(2),
//                "no note",
//                requests
//        );
    }
}
