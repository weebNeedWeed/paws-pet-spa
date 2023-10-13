package com.paws.persistence.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "detailed_appoinment_items")
public class DetailedAppointmentItem {
    @Id
    private Long id;

    @Column(nullable = false)
    private float petWeight;

    private LocalDateTime estimatedEndingTime;

    private LocalDateTime actualStartingTime;

    private LocalDateTime actualEndingTime;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "appointment_id")
    private AppointmentItem appointmentItem;
}
