package com.paws.controllers;

import com.paws.exceptions.AppointmentItemNotFoundException;
import com.paws.exceptions.BillNotFoundException;
import com.paws.exceptions.InvalidPaymentResultException;
import com.paws.models.websockets.WebSocketMessage;
import com.paws.payloads.request.ValidatePaymentResultRequest;
import com.paws.payloads.response.BillDto;
import com.paws.services.appointments.AppointmentService;
import com.paws.services.payments.PaymentService;
import com.paws.services.urlShortens.UrlShortenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final UrlShortenService urlShortenService;
    private final PaymentService paymentService;
    private final AppointmentService appointmentService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public HomeController(UrlShortenService urlShortenService, PaymentService paymentService, AppointmentService appointmentService, SimpMessagingTemplate messagingTemplate) {
        this.urlShortenService = urlShortenService;
        this.paymentService = paymentService;
        this.appointmentService = appointmentService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("")
    public String index() {
        return "home/index";
    }

    @GetMapping("/shorten/{name}")
    public String redirectToRawUrl(@PathVariable("name") String name) {
        String raw = urlShortenService.getRawUrl(name);
        if(raw == null) {
            return "error/error-404";
        }

        return "redirect:" + raw;
    }

    @GetMapping("/vnp_return")
    public String validatePaymentResult(ValidatePaymentResultRequest request, Model model) throws InvalidPaymentResultException, AppointmentItemNotFoundException, BillNotFoundException {
        paymentService.validatePaymentResult(request);

        long billId = Long.parseLong(request.getVnp_TxnRef());
        appointmentService.setPaidBill(Long.parseLong(request.getVnp_TxnRef()));

        BillDto bill = appointmentService.getBill(billId);

        messagingTemplate.convertAndSendToUser(bill.getEmployee().getUsername(),
                "/queue/appointments", new WebSocketMessage<>(null, "payment_success"));

        model.addAttribute("bill", bill);
        return "appointments/bill";
    }
}
