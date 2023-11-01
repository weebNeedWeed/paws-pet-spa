package com.paws.dataseeders;

import com.paws.entities.PetType;
import com.paws.repositories.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PetTypeDataSeeder implements CommandLineRunner {
    private final PetTypeRepository petTypeRepository;

    @Autowired
    public PetTypeDataSeeder(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(petTypeRepository.count() == 0) {
            PetType dog = new PetType();
            dog.setName("Dog");
            petTypeRepository.save(dog);

            PetType cat = new PetType();
            cat.setName("Cat");
            petTypeRepository.save(cat);
        }
    }
}
