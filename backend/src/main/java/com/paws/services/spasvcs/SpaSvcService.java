package com.paws.services.spasvcs;

import com.paws.exceptions.InvalidWeightRangeException;
import com.paws.exceptions.SpaServiceNameAlreadyExistsException;
import com.paws.exceptions.SpaServiceNotFoundException;
import com.paws.payloads.response.ServiceDetailDto;
import com.paws.payloads.response.SpaSvcDto;

import java.math.BigDecimal;
import java.util.List;

public interface SpaSvcService {
    void addNew(String name,
                 String description,
                 BigDecimal defaultPrice,
                 float defaultEstimatedCompletionMinutes) throws SpaServiceNameAlreadyExistsException;

    void delete(long serviceId) throws SpaServiceNotFoundException;

    List<SpaSvcDto> getAll();

    SpaSvcDto getById(long serviceId);

    void updateDetailByWeightRange(
            long serviceId,
            BigDecimal minWeight,
            BigDecimal maxWeight,
            BigDecimal price,
            float estimatedCompletionMinutes) throws SpaServiceNotFoundException, InvalidWeightRangeException;

    void update(
            long serviceId,
            String name,
            String description,
            BigDecimal defaultPrice,
            float defaultEstimatedCompletionMinutes) throws SpaServiceNotFoundException, SpaServiceNameAlreadyExistsException;

    List<ServiceDetailDto> getAllDetails(long serviceId) throws SpaServiceNotFoundException;

    void deleteDetail(long serviceId, BigDecimal minWeight, BigDecimal maxWeight) throws SpaServiceNotFoundException;
}
