package com.paws.payloads.response;

import com.paws.entities.Bill;
import com.paws.entities.PetType;
import com.paws.entities.SpaService;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillItemDto {
    private Long id;

    private String petName;

    private double petWeight;

    private BigDecimal price;

    private SpaSvcDto spaService;

    private PetTypeDto petType;
}
