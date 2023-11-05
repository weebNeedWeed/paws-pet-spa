package com.paws.controllers;

import com.paws.services.common.payloads.PagedResult;
import com.paws.services.employees.EmployeeService;
import com.paws.services.employees.payloads.AppointmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    private final EmployeeService employeeService;

    @Autowired
    public AppointmentController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public String index(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Model model) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        PagedResult<AppointmentDto> result = employeeService.getAllAppointments(pageable);

        model.addAttribute("pagedResult", result);

        return "appointments/index";
    }
}
