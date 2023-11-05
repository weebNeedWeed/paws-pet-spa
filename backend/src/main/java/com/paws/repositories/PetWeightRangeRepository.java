package com.paws.repositories;

import com.paws.entities.PetWeightRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface PetWeightRangeRepository extends JpaRepository<PetWeightRange, PetWeightRange.PetWeightRangeId> {
    @Query("select pw from PetWeightRange pw where pw.id.minWeight = :min AND pw.id.maxWeight = :max")
    PetWeightRange getExactPetWeightRangeByMinAndMax(BigDecimal min, BigDecimal max);

    @Query("select pw from PetWeightRange pw where :weight between pw.id.minWeight AND pw.id.maxWeight order by pw.id.minWeight asc limit 1")
    PetWeightRange getPetWeightRangeByWeight(BigDecimal weight);
}
