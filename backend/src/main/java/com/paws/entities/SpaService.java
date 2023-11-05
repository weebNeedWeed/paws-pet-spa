package com.paws.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "spa_services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpaService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal defaultPrice;

    @Column(nullable = false)
    private float defaultEstimatedCompletionMinutes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaService", orphanRemoval = true)
    private List<SpaServiceDetail> spaServiceDetails = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "spaServices", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<AppointmentItem> appointmentItems = new HashSet<>();

    public void addDetail(SpaServiceDetail detail) {
        this.spaServiceDetails.add(detail);
        detail.setSpaService(this);
    }
}
