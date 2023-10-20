package com.paws.application.services.spasvcs;

import com.paws.application.common.errors.SpaServiceNameAlreadyExistsException;
import com.paws.application.common.errors.SpaServiceNotFoundException;
import com.paws.application.services.spasvcs.common.ServicePricingByWeightRange;
import com.paws.application.services.spasvcs.common.SpaSvcDto;

import java.util.List;

public interface SpaSvcService {
    SpaSvcDto addService(String name,
                         String description,
                         float defaultPrice,
                         float defaultEstimatedCompletionMinutes) throws SpaServiceNameAlreadyExistsException;

    void deleteService(long serviceId) throws SpaServiceNotFoundException;

    List<SpaSvcDto> getAllServices();

    void updatePricingByWeightRange(
            long serviceId,
            List<ServicePricingByWeightRange> pricingList) throws SpaServiceNotFoundException;

    void updateService(
            long serviceId,
            String name,
            String description,
            float defaultPrice,
            float defaultEstimatedCompletionMinutes);
}
