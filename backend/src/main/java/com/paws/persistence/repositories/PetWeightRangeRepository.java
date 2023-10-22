package com.paws.persistence.repositories;

import com.paws.persistence.entities.PetWeightRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface PetWeightRangeRepository extends JpaRepository<PetWeightRange, PetWeightRange.PetWeightRangeId> {
    @Query("select pw from PetWeightRange pw where pw.id.minWeight = :min AND pw.id.maxWeight = :max")
    PetWeightRange getExactPetWeightRangeByMinAndMax(BigDecimal min, BigDecimal max);
}
