package com.paws.repositories;

import com.paws.entities.DetailedAppointmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailedAppointmentItemRepository extends JpaRepository<DetailedAppointmentItem, Long> {
    @Query("select di from DetailedAppointmentItem di where di.id = :id")
    DetailedAppointmentItem findById(long id);
}
