package com.paws.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "appointment_items")
@Getter
@Setter
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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
