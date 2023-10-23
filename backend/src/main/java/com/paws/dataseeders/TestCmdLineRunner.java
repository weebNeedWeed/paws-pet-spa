package com.paws.dataseeders;

import com.paws.services.spasvcs.SpaSvcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestCmdLineRunner implements CommandLineRunner {
    @Autowired
    private SpaSvcService spaSvcService;

    @Override
    public void run(String... args) throws Exception {
//        spaSvcService.updateDetailByWeightRange(1, BigDecimal.valueOf(2.5), BigDecimal.valueOf(4.9), 1, 1);
    }
}
