package com.paws.entities;

import com.paws.entities.common.enums.BillStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false)
    private float totalAmount;

    @Column(nullable = false)
    private BillStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
