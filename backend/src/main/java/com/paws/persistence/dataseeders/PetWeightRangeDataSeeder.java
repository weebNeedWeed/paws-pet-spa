package com.paws.persistence.dataseeders;

import com.paws.persistence.entities.PetWeightRange;
import com.paws.persistence.repositories.PetWeightRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class PetWeightRangeDataSeeder implements CommandLineRunner {
    private final PetWeightRangeRepository petWeightRangeRepository;

    @Autowired
    public PetWeightRangeDataSeeder(PetWeightRangeRepository petWeightRangeRepository) {
        this.petWeightRangeRepository = petWeightRangeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(petWeightRangeRepository.count() == 0) {
            BigDecimal[][] ranges = new BigDecimal[][] {
                    {BigDecimal.valueOf(0), BigDecimal.valueOf(2.4f)},
                    {BigDecimal.valueOf(2.5f), BigDecimal.valueOf(4.9f)},
                    {BigDecimal.valueOf(5), BigDecimal.valueOf(9.9f)},
                    {BigDecimal.valueOf(10), BigDecimal.valueOf(15)}
            };

            for(BigDecimal[] range : ranges) {
                PetWeightRange weightRange = new PetWeightRange();
                weightRange.setId(new PetWeightRange.PetWeightRangeId());
                weightRange.getId().setMinWeight(range[0]);
                weightRange.getId().setMaxWeight(range[1]);

                petWeightRangeRepository.save(weightRange);
            }
        }
    }
}
