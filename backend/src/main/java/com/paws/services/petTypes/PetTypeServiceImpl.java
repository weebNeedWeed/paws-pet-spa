package com.paws.services.petTypes;

import com.paws.entities.PetType;
import com.paws.payloads.response.PetTypeDto;
import com.paws.repositories.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetTypeServiceImpl implements PetTypeService{
    private final PetTypeRepository petTypeRepository;

    @Autowired
    public PetTypeServiceImpl(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public List<PetTypeDto> getAll() {
        return petTypeRepository.findAll().stream().map(x -> {
            PetTypeDto dto = new PetTypeDto();
            dto.setId(x.getId());
            dto.setName(x.getName());

            return dto;
        }).toList();
    }
}
