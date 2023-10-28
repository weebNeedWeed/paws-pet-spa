package com.paws.services.spasvcs;

import com.paws.exceptions.InvalidWeightRangeException;
import com.paws.exceptions.SpaServiceNameAlreadyExistsException;
import com.paws.exceptions.SpaServiceNotFoundException;
import com.paws.services.spasvcs.payloads.ServiceDetailDto;
import com.paws.services.spasvcs.payloads.SpaSvcDto;

import java.math.BigDecimal;
import java.util.List;

public interface SpaSvcService {
    SpaSvcDto addService(String name,
                         String description,
                         BigDecimal defaultPrice,
                         float defaultEstimatedCompletionMinutes) throws SpaServiceNameAlreadyExistsException;

    void deleteService(long serviceId) throws SpaServiceNotFoundException;

    List<SpaSvcDto> getAllServices();

    SpaSvcDto getServiceById(long serviceId);

    void updateDetailByWeightRange(
            long serviceId,
            BigDecimal minWeight,
            BigDecimal maxWeight,
            BigDecimal price,
            float estimatedCompletionMinutes) throws SpaServiceNotFoundException, InvalidWeightRangeException;

    void updateService(
            long serviceId,
            String name,
            String description,
            BigDecimal defaultPrice,
            float defaultEstimatedCompletionMinutes) throws SpaServiceNotFoundException, SpaServiceNameAlreadyExistsException;

    List<ServiceDetailDto> getAllDetails(long serviceId) throws SpaServiceNotFoundException;

    void deleteDetail(long serviceId, BigDecimal minWeight, BigDecimal maxWeight) throws SpaServiceNotFoundException;
}
