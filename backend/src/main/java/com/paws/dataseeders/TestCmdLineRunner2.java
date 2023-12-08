package com.paws.dataseeders;

import com.paws.entities.Bill;
import com.paws.repositories.BillRepository;
import com.paws.services.payments.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestCmdLineRunner2 implements CommandLineRunner {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BillRepository billRepository;

    @Override
    public void run(String... args) throws Exception {
//        Bill bill = billRepository.findById(1L).orElseThrow();
//        System.out.println(paymentService.createPaymentUrl(bill,"42.112.230.23"));
    }
}
