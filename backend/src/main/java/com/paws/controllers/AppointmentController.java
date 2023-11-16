package com.paws.controllers;

import com.paws.entities.common.enums.AppointmentItemStatus;
import com.paws.exceptions.AppointmentNotFoundException;
import com.paws.services.common.payloads.PagedResult;
import com.paws.services.employees.EmployeeService;
import com.paws.services.employees.payloads.AppointmentDto;
import com.paws.services.employees.payloads.AppointmentItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("{appointmentId}/details")
    public String details(@PathVariable("appointmentId") long appointmentId, Model model) {
        AppointmentDto appointment;
        try {
            appointment = employeeService.getAppointmentDetails(appointmentId);
        } catch(AppointmentNotFoundException ex) {
            return "redirect:/appointments";
        }

        model.addAttribute("appointment", appointment);

        return "appointments/details";
    }

    @GetMapping("{appointmentId}/{appointmentItemId}/start")
    public String start(@PathVariable("appointmentId") long appointmentId,
                        @PathVariable("appointmentItemId") long appointmentItemId,
                        Model model) {
        AppointmentDto appointment;
        try {
            appointment = employeeService.getAppointmentDetails(appointmentId);
        } catch(AppointmentNotFoundException ex) {
            return "redirect:/appointments";
        }

        AppointmentItemDto item = appointment.getAppointmentItems().stream().filter(x -> x.getId() == appointmentItemId).findFirst().orElse(null);

        boolean isProcessing = item.getStatus() != null &&
                item.getStatus() != AppointmentItemStatus.DONE;

        model.addAttribute("isProcessing", isProcessing);
        model.addAttribute("appointment", appointment);
        model.addAttribute("appointmentItem", item);

        return "appointments/start";
    }

    @GetMapping("{appointmentId}/{appointmentItemId}/assign-employee")
    public String assignEmployee(@PathVariable("appointmentId") long appointmentId,
                                 @PathVariable("appointmentItemId") long appointmentItemId,
                                 Model model) {
        return "appointments/assignEmployee";
    }
}
