package com.paws.entities;

import com.paws.entities.common.enums.AppointmentItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "detailed_appoinment_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailedAppointmentItem {
    @Id
    private Long id;

    private AppointmentItemStatus status;

    private Double petWeight;

    private LocalDateTime estimatedEndingTime;

    private LocalDateTime actualStartingTime;

    private LocalDateTime actualEndingTime;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "appointment_id")
    private AppointmentItem appointmentItem;
}
