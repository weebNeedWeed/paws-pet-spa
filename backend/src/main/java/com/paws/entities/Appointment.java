package com.paws.entities;

import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.entities.common.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appoinments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private AppointmentLocation location;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    private String note;

    @Column(nullable = false)
    private AppointmentStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "appointment", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AppointmentItem> appointmentItems = new ArrayList<>();

    public void addItem(AppointmentItem item) {
        appointmentItems.add(item);
        item.setAppointment(this);
    }
}
