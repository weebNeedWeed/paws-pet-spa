package com.paws.services.weightranges;

import com.paws.services.weightranges.payloads.WeightRangeDto;

import java.util.List;

public interface WeightRangeService {
    List<WeightRangeDto> getAllWeightRanges();
}
