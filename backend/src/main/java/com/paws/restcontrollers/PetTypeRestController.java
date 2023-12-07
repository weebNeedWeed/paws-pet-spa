package com.paws.restcontrollers;

import com.paws.services.petTypes.PetTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pet-types")
@CrossOrigin(origins = "*")
public class PetTypeRestController {
    private final PetTypeService petTypeService;

    public PetTypeRestController(PetTypeService petTypeService) {
        this.petTypeService = petTypeService;
    }

    @GetMapping("")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> getAllTypes() {
        return ResponseEntity.ok().body(petTypeService.getAll());
    }
}
