package com.paws.services.weightranges;

import com.paws.entities.PetWeightRange;
import com.paws.repositories.PetWeightRangeRepository;
import com.paws.payloads.response.WeightRangeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightRangeServiceImpl implements WeightRangeService{
    private final PetWeightRangeRepository petWeightRangeRepository;

    public WeightRangeServiceImpl(PetWeightRangeRepository petWeightRangeRepository) {
        this.petWeightRangeRepository = petWeightRangeRepository;
    }

    @Override
    public List<WeightRangeDto> getAllWeightRanges() {
        List<PetWeightRange> petWeightRanges = petWeightRangeRepository.findAll();
        List<WeightRangeDto> weightRangeDtos = petWeightRanges.stream()
                .map(x -> {
                    WeightRangeDto dto = new WeightRangeDto();
                    dto.setMinWeight(x.getId().getMinWeight());
                    dto.setMaxWeight(x.getId().getMaxWeight());

                    return dto;
                }).toList();
        return weightRangeDtos;
    }
}
