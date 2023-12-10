package com.paws.controllers;

import com.paws.entities.common.enums.AppointmentItemStatus;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.exceptions.*;
import com.paws.payloads.common.PagedResult;
import com.paws.payloads.response.BillDto;
import com.paws.services.appointments.AppointmentService;
import com.paws.services.employees.EmployeeService;
import com.paws.payloads.response.AppointmentDto;
import com.paws.payloads.response.AppointmentItemDto;
import com.paws.payloads.response.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
@RequestMapping("/appointments")
@PreAuthorize("hasAuthority('COORDINATOR')")
public class AppointmentController {
    private final EmployeeService employeeService;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(EmployeeService employeeService, AppointmentService appointmentService) {
        this.employeeService = employeeService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public String index(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Model model) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        PagedResult<AppointmentDto> result = appointmentService.getAll(pageable);

        model.addAttribute("pagedResult", result);

        return "appointments/index";
    }

    @GetMapping("{appointmentId}/details")
    @PreAuthorize("isAuthenticated()")
    public String details(@PathVariable("appointmentId") long appointmentId, Model model, Principal principal) throws AppointmentNotFoundException {
        AppointmentDto appointment = appointmentService.getDetails(appointmentId);

        // If the appointment has the status of pending and its items has been assigned with one employee
        // then canApprove = true;
        boolean canApprove = appointment.getStatus() == AppointmentStatus.PENDING;
        for(AppointmentItemDto item: appointment.getAppointmentItems()) {
            if (item.getEmployee() == null) {
                canApprove = false;
                break;
            }
        }

        boolean canAssignEmp = appointment.getStatus() == AppointmentStatus.PENDING;

        model.addAttribute("canApprove", canApprove);
        model.addAttribute("canAssignEmp", canAssignEmp);
        model.addAttribute("appointment", appointment);

        return "appointments/details";
    }

    @GetMapping("{appointmentId}/{appointmentItemId}/start")
    @PreAuthorize("hasAuthority('NORMAL_EMPLOYEE')")
    public String start(@PathVariable("appointmentId") long appointmentId,
                        @PathVariable("appointmentItemId") long appointmentItemId,
                        Model model, Principal principal) throws AppointmentNotFoundException, AppointmentItemNotFoundException, InvalidAppointmentStatusException {
        AppointmentDto appointment = appointmentService.getDetails(appointmentId);

        AppointmentItemDto item = appointment.getAppointmentItems().stream()
                .filter(x -> x.getId() == appointmentItemId).findFirst()
                .orElseThrow(AppointmentItemNotFoundException::new);

        if(appointment.getStatus() == AppointmentStatus.PENDING
                || appointment.getStatus() == AppointmentStatus.COMPLETED
                || appointment.getStatus() == AppointmentStatus.CANCELED) {
            throw new InvalidAppointmentStatusException(AppointmentStatus.SCHEDULED, appointment.getStatus());
        }

        if(item.getEmployee() == null
                || !Objects.equals(item.getEmployee().getUsername(), principal.getName())) {
            throw new AccessDeniedException("Chỉ nhân viên được phân công mới có thể truy cập");
        }

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
                                 Model model) throws AppointmentNotFoundException, AppointmentItemNotFoundException {
        AppointmentDto appointment;
        appointment = appointmentService.getDetails(appointmentId);

        AppointmentItemDto item = appointment.getAppointmentItems().stream()
                .filter(x -> x.getId() == appointmentItemId).findFirst()
                .orElseThrow(AppointmentItemNotFoundException::new);

        Set<String> requiredRoles = new HashSet<>();
        requiredRoles.add("NORMAL_EMPLOYEE");
        List<EmployeeDto> employees = employeeService.getAll(requiredRoles);

        long selectedEmp = item.getEmployee() == null ? -1 : item.getEmployee().getId();

        model.addAttribute("selectedEmpId", selectedEmp);
        model.addAttribute("appointment", appointment);
        model.addAttribute("appointmentItem", item);
        model.addAttribute("employees", employees);

        return "appointments/assignEmployee";
    }

    @PostMapping("{appointmentId}/{appointmentItemId}/assign-employee")
    public String assignEmployee(@PathVariable("appointmentId") long appointmentId,
                                 @PathVariable("appointmentItemId") long appointmentItemId,
                                 @RequestParam("employeeId") long employeeId,
                                 RedirectAttributes redirectAttributes) throws AppointmentItemNotFoundException, AppointmentNotFoundException, EmployeeNotFoundException, InvalidAppointmentStatusException {
        appointmentService.assignEmployee(appointmentId, appointmentItemId, employeeId);

        redirectAttributes.addAttribute("appointmentId", appointmentId);
        return "redirect:/appointments/{appointmentId}/details";
    }

    @PostMapping("{appointmentId}/approve")
    public String approveAppointment(@PathVariable("appointmentId") long appointmentId, RedirectAttributes redirectAttributes) throws CannotScheduleAppointmentException, NoEmployeeAssignedException, InvalidAppointmentStatusException, AppointmentNotFoundException {
        appointmentService.approve(appointmentId);

        redirectAttributes.addAttribute("appointmentId", appointmentId);
        return "redirect:/appointments/{appointmentId}/details";
    }

    @GetMapping("{appointmentId}/bill")
    @PreAuthorize("isAuthenticated()")
    public String showBill(@PathVariable("appointmentId") long appointmentId, Model model) throws BillNotFoundException {
        BillDto bill = appointmentService.getBill(appointmentId);
        model.addAttribute("bill", bill);

        return "appointments/bill";
    }
}
