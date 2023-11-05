package com.paws.repositories;

import com.paws.entities.AppointmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentItemRepository extends JpaRepository<AppointmentItem, Long> {
    @Query("select ai from AppointmentItem ai where ai.id = :id")
    AppointmentItem findById(long id);
}
