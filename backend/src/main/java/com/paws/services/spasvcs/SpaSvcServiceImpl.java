package com.paws.services.spasvcs;

import com.paws.exceptions.InvalidWeightRangeException;
import com.paws.exceptions.SpaServiceNameAlreadyExistsException;
import com.paws.exceptions.SpaServiceNotFoundException;
import com.paws.services.spasvcs.common.SpaSvcDto;
import com.paws.entities.PetWeightRange;
import com.paws.entities.SpaService;
import com.paws.entities.SpaServiceDetail;
import com.paws.repositories.PetWeightRangeRepository;
import com.paws.repositories.SpaServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SpaSvcServiceImpl implements SpaSvcService{
    private final SpaServiceRepository spaServiceRepository;
    private final PetWeightRangeRepository petWeightRangeRepository;

    @Autowired
    public SpaSvcServiceImpl(SpaServiceRepository spaServiceRepository, PetWeightRangeRepository petWeightRangeRepository) {
        this.spaServiceRepository = spaServiceRepository;
        this.petWeightRangeRepository = petWeightRangeRepository;
    }

    @Override
    public SpaSvcDto addService(String name, String description, BigDecimal defaultPrice, float defaultEstimatedCompletionMinutes) throws SpaServiceNameAlreadyExistsException {
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
    public SpaSvcDto getServiceById(long serviceId) {
        SpaService service = spaServiceRepository.getSpaServiceById(serviceId);
        if(service == null) {
            return null;
        }

        return mapSpaServiceToSpaSvcDto(service);
    }

    @Override
    @Transactional
    public void updateDetailByWeightRange(long serviceId, BigDecimal minWeight, BigDecimal maxWeight, BigDecimal price, float estimatedCompletionMinutes) throws SpaServiceNotFoundException, InvalidWeightRangeException {
        SpaService service = spaServiceRepository.getSpaServiceById(serviceId);
        if(service == null) {
            throw new SpaServiceNotFoundException();
        }

        PetWeightRange weightRange = petWeightRangeRepository.getExactPetWeightRangeByMinAndMax(minWeight, maxWeight);
        if(weightRange == null) {
            throw new InvalidWeightRangeException(minWeight, maxWeight);
        }

        SpaServiceDetail spaServiceDetail = spaServiceRepository.getSpaServiceDetail(serviceId, minWeight, maxWeight);
        if(spaServiceDetail == null) {
            spaServiceDetail = new SpaServiceDetail();
            spaServiceDetail.setPetWeightRange(weightRange);
            spaServiceDetail.setSpaService(service);
            spaServiceDetail.setSpaServiceDetailId(new SpaServiceDetail.SpaServiceDetailId());
        }

        spaServiceDetail.setPrice(price);
        spaServiceDetail.setEstimatedCompletionMinutes(estimatedCompletionMinutes);

        service.addDetail(spaServiceDetail);

        spaServiceRepository.save(service);
    }

    @Override
    public void updateService(long serviceId, String name, String description, BigDecimal defaultPrice, float defaultEstimatedCompletionMinutes) throws SpaServiceNotFoundException, SpaServiceNameAlreadyExistsException {
        SpaService service = spaServiceRepository.getSpaServiceById(serviceId);
        if(service == null) {
            throw new SpaServiceNotFoundException();
        }

        if(spaServiceRepository.existsSpaServiceByNameEqualsExcepts(name ,service.getName())) {
            throw new SpaServiceNameAlreadyExistsException();
        }

        service.setName(name);
        service.setDescription(description);
        service.setDefaultPrice(defaultPrice);
        service.setDefaultEstimatedCompletionMinutes(defaultEstimatedCompletionMinutes);

        spaServiceRepository.save(service);
    }

    private SpaSvcDto mapSpaServiceToSpaSvcDto(SpaService spaService) {
        SpaSvcDto spaSvcDto = SpaSvcDto.builder()
                        .id(spaService.getId())
                        .name(spaService.getName())
                        .description(spaService.getDescription())
                        .defaultPrice(spaService.getDefaultPrice())
                        .defaultEstimatedCompletionMinutes(spaService.getDefaultEstimatedCompletionMinutes()).build();

        return spaSvcDto;
    }
}
