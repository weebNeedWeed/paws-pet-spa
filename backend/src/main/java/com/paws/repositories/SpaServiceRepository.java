package com.paws.repositories;

import com.paws.entities.SpaService;
import com.paws.entities.SpaServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SpaServiceRepository extends JpaRepository<SpaService, Long> {
    @Query("select case when count(1) = 1 then true else false end from SpaService ss where ss.name = :name")
    boolean existsSpaServiceByName(String name);

    @Query("select ss from SpaService ss where ss.id = :id")
    SpaService getSpaServiceById(long id);

    @Query("select ss from SpaService ss")
    List<SpaService> getAllSpaServices();

    @Query("select case when count(1) = 1 then true else false end from SpaService ss where ss.name = :equal and ss.name <> :except")
    boolean existsSpaServiceByNameEqualsExcepts(String equal, String except);

    @Query("select ssd from SpaService ss inner join ss.spaServiceDetails ssd where ss.id = :serviceId and ssd.spaServiceDetailId.petWeightRangeId.minWeight = :min and ssd.spaServiceDetailId.petWeightRangeId.maxWeight = :max")
    SpaServiceDetail getSpaServiceDetail(long serviceId, BigDecimal min, BigDecimal max);
}
