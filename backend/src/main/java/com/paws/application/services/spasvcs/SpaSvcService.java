package com.paws.application.services.spasvcs;

import com.paws.application.common.errors.InvalidWeightRangeException;
import com.paws.application.common.errors.SpaServiceNameAlreadyExistsException;
import com.paws.application.common.errors.SpaServiceNotFoundException;
import com.paws.application.services.spasvcs.common.ServicePricingByWeightRange;
import com.paws.application.services.spasvcs.common.SpaSvcDto;

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
}
