package com.paws.persistence.repositories;

import com.paws.persistence.entities.SpaService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaServiceRepository extends JpaRepository<SpaService, Long> {
    @Query("select case when count(1) = 1 then true else false end from SpaService ss where ss.name = :name")
    boolean existsSpaServiceByName(String name);

    @Query("select ss from SpaService ss where ss.id = :id")
    SpaService getSpaServiceById(long id);

    @Query("select ss from SpaService ss")
    List<SpaService> getAllSpaServices();
}
