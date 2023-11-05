package com.paws.dataseeders;

import com.paws.entities.SpaService;
import com.paws.repositories.SpaServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SpaServiceDataSeeder implements CommandLineRunner {
    private final SpaServiceRepository spaServiceRepository;

    public SpaServiceDataSeeder(SpaServiceRepository spaServiceRepository) {
        this.spaServiceRepository = spaServiceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(spaServiceRepository.count() == 0) {
            SpaService service = new SpaService();
            service.setName("Tắm");
            service.setDescription("Dịch vụ tắm, sấy cho chó và mèo.");
            service.setDefaultPrice(BigDecimal.valueOf(150000));
            service.setDefaultEstimatedCompletionMinutes(30);

            spaServiceRepository.save(service);
        }
    }
}
