package com.paws.services.petTypes;

import com.paws.payloads.response.PetTypeDto;

import java.util.List;

public interface PetTypeService {
    List<PetTypeDto> getAll();
}
