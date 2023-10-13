package com.paws.persistence.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "spa_services")
public class SpaService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private float defaultPrice;

    @Column(nullable = false)
    private float defaultEstimatedCompletionMinutes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaService", orphanRemoval = true)
    private List<SpaServicePricing> spaServicePricingList = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "spaServices")
    private Set<AppointmentItem> appointmentItems = new HashSet<>();
}
