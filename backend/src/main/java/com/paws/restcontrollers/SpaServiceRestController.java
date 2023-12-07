package com.paws.restcontrollers;

import com.paws.payloads.response.SpaSvcDto;
import com.paws.services.spasvcs.SpaSvcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/services")
public class SpaServiceRestController {
    private final SpaSvcService spaSvcService;

    @Autowired
    public SpaServiceRestController(SpaSvcService spaSvcService) {
        this.spaSvcService = spaSvcService;
    }

    @GetMapping("")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> getAllServices() {
        List<SpaSvcDto> services = spaSvcService.getAll();
        return ResponseEntity.ok().body(services);
    }
}
