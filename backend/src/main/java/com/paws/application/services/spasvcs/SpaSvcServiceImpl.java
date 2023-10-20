package com.paws.application.services.spasvcs;

import com.paws.application.common.errors.SpaServiceNameAlreadyExistsException;
import com.paws.application.common.errors.SpaServiceNotFoundException;
import com.paws.application.services.spasvcs.common.ServicePricingByWeightRange;
import com.paws.application.services.spasvcs.common.SpaSvcDto;
import com.paws.persistence.entities.SpaService;
import com.paws.persistence.repositories.SpaServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaSvcServiceImpl implements SpaSvcService{
    private final SpaServiceRepository spaServiceRepository;

    @Autowired
    public SpaSvcServiceImpl(SpaServiceRepository spaServiceRepository) {
        this.spaServiceRepository = spaServiceRepository;
    }

    @Override
    public SpaSvcDto addService(String name, String description, float defaultPrice, float defaultEstimatedCompletionMinutes) throws SpaServiceNameAlreadyExistsException {
        if(spaServiceRepository.existsSpaServiceByName(name)) {
            throw new SpaServiceNameAlreadyExistsException();
        }

        SpaService service = new SpaService();
        service.setName(name);
        service.setDefaultPrice(defaultPrice);
        service.setDescription(description);
        service.setDefaultEstimatedCompletionMinutes(defaultEstimatedCompletionMinutes);

        spaServiceRepository.save(service);

        return mapSpaServiceToSpaSvcDto(service);
    }

    @Override
    public void deleteService(long serviceId) throws SpaServiceNotFoundException {
        SpaService service = spaServiceRepository.getSpaServiceById(serviceId);
        if(service == null) {
            throw new SpaServiceNotFoundException();
        }

        spaServiceRepository.delete(service);
    }

    @Override
    public List<SpaSvcDto> getAllServices() {
        List<SpaService> spaServices = spaServiceRepository.getAllSpaServices();
        return spaServices.stream().map(sv -> mapSpaServiceToSpaSvcDto(sv)).toList();
    }

    @Override
    public void updatePricingByWeightRange(long serviceId, List<ServicePricingByWeightRange> pricingList) throws SpaServiceNotFoundException {
        SpaService service = spaServiceRepository.getSpaServiceById(serviceId);
        if(service == null) {
            throw new SpaServiceNotFoundException();
        }
    }

    @Override
    public void updateService(long serviceId, String name, String description, float defaultPrice, float defaultEstimatedCompletionMinutes) {

    }

    private SpaSvcDto mapSpaServiceToSpaSvcDto(SpaService spaService) {
        SpaSvcDto spaSvcDto = new SpaSvcDto();
        spaSvcDto.setId(spaService.getId());
        spaSvcDto.setName(spaService.getName());
        spaSvcDto.setDescription(spaService.getDescription());
        spaSvcDto.setDefaultPrice(spaService.getDefaultPrice());
        spaSvcDto.setDefaultEstimatedCompletionMinutes(spaService.getDefaultEstimatedCompletionMinutes());

        return spaSvcDto;
    }
}
