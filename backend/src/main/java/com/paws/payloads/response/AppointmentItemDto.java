package com.paws.payloads.response;

import com.paws.entities.common.enums.AppointmentItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentItemDto {
    private long id;
    private String petName;
    private String petType;
    private AppointmentItemStatus status;
    private Double petWeight;
    private LocalDateTime estimatedEndingTime;
    private LocalDateTime actualStartingTime;
    private LocalDateTime endingTime;
    private LocalDateTime currentServiceEndingTime;
    private Integer doneServiceIndex;
    private EmployeeDto employee;

    List<SpaSvcDto> spaServices;
}
