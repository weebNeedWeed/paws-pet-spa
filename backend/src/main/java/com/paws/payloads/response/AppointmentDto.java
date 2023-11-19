package com.paws.payloads.response;

import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.entities.common.enums.AppointmentStatus;
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
public class AppointmentDto {
    private long id;
    private AppointmentLocation location;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String customerFullName;
    private String note;
    private List<AppointmentItemDto> appointmentItems;
}
