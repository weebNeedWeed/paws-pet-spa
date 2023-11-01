package com.paws.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "appointment_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String petName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pet_type_id")
    private PetType petType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "appoinment_item_spa_service",
            joinColumns = {@JoinColumn(name = "appointment_item_id")},
            inverseJoinColumns = {@JoinColumn(name = "spa_service_id")})
    private Set<SpaService> spaServices = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "appointmentItems")
    private Set<Employee> employees = new HashSet<>();
}
