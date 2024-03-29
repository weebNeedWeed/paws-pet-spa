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

    private LocalDateTime endingTime;

    private LocalDateTime actualStartingTime;

    private LocalDateTime currentServiceEndingTime;

    private Integer doneServiceIndex;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_item_id")
    private AppointmentItem appointmentItem;
}
