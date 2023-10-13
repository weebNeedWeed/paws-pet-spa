package com.paws.persistence.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "spa_service_pricing")
public class SpaServicePricing {
    @EmbeddedId
    private SpaServicePricingId spaServicePricingId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("spaServiceId")
    private SpaService spaService;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "min_weight"),
            @JoinColumn(name = "max_weight")
    })
    @MapsId("petWeightRangeId")
    private PetWeightRange petWeightRange;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private float estimatedCompletionMinutes;

    @Embeddable
    public static class SpaServicePricingId implements Serializable {
        @Column(name = "spa_service_id")
        private Long spaServiceId;
        private PetWeightRange.PetWeightRangeId petWeightRangeId;
    }
}
