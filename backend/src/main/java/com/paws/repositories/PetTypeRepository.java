package com.paws.repositories;

import com.paws.entities.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long> {
    @Query("select pt from PetType pt where pt.id = :id")
    PetType findById(long id);
}
