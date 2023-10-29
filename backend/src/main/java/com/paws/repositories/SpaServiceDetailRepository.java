package com.paws.repositories;

import com.paws.entities.SpaServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface SpaServiceDetailRepository extends JpaRepository<SpaServiceDetail, SpaServiceDetail.SpaServiceDetailId> {

    @Modifying
    @Transactional
    @Query("delete from SpaServiceDetail ssd where ssd.spaServiceDetailId.spaServiceId = :serviceId and ssd.spaServiceDetailId.petWeightRangeId.minWeight = :minWeight and ssd.spaServiceDetailId.petWeightRangeId.maxWeight = :maxWeight")
    void deleteSpaServiceDetail(long serviceId, BigDecimal minWeight, BigDecimal maxWeight);
}
