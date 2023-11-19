package com.paws.services.weightranges;

import com.paws.payloads.response.WeightRangeDto;

import java.util.List;

public interface WeightRangeService {
    List<WeightRangeDto> getAllWeightRanges();
}
