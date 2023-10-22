package com.paws.persistence.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
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

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal defaultPrice;

    @Column(nullable = false)
    private float defaultEstimatedCompletionMinutes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaService", orphanRemoval = true)
    private List<SpaServiceDetail> spaServiceDetails = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "spaServices")
    private Set<AppointmentItem> appointmentItems = new HashSet<>();

    public SpaService() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public float getDefaultEstimatedCompletionMinutes() {
        return defaultEstimatedCompletionMinutes;
    }

    public void setDefaultEstimatedCompletionMinutes(float defaultEstimatedCompletionMinutes) {
        this.defaultEstimatedCompletionMinutes = defaultEstimatedCompletionMinutes;
    }

    public void addDetail(SpaServiceDetail detail) {
        this.spaServiceDetails.add(detail);
        detail.setSpaService(this);
    }
}
