package com.paws.services.customers.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeAppointmentItemRequest {
    private String petName;
    private long petTypeId;

    private List<Long> serviceIds;
}
