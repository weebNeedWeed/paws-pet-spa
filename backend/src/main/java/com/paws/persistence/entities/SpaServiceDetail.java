package com.paws.persistence.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "spa_service_details")
public class SpaServiceDetail {
    @EmbeddedId
    private SpaServiceDetailId spaServiceDetailId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("spaServiceId")
    private SpaService spaService;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "min_weight", referencedColumnName = "min_weight"),
            @JoinColumn(name = "max_weight", referencedColumnName = "max_weight")
    })
    @MapsId("petWeightRangeId")
    private PetWeightRange petWeightRange;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private float estimatedCompletionMinutes;

    @Embeddable
    public static class SpaServiceDetailId implements Serializable {
        @Column(name = "spa_service_id")
        private Long spaServiceId;
        private PetWeightRange.PetWeightRangeId petWeightRangeId;

        public SpaServiceDetailId() {
        }

        public Long getSpaServiceId() {
            return spaServiceId;
        }

        public void setSpaServiceId(Long spaServiceId) {
            this.spaServiceId = spaServiceId;
        }

        public PetWeightRange.PetWeightRangeId getPetWeightRangeId() {
            return petWeightRangeId;
        }

        public void setPetWeightRangeId(PetWeightRange.PetWeightRangeId petWeightRangeId) {
            this.petWeightRangeId = petWeightRangeId;
        }
    }

    public SpaServiceDetail() {
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public float getEstimatedCompletionMinutes() {
        return estimatedCompletionMinutes;
    }

    public void setEstimatedCompletionMinutes(float estimatedCompletionMinutes) {
        this.estimatedCompletionMinutes = estimatedCompletionMinutes;
    }

    public SpaService getSpaService() {
        return spaService;
    }

    public void setSpaService(SpaService spaService) {
        this.spaService = spaService;
    }

    public PetWeightRange getPetWeightRange() {
        return petWeightRange;
    }

    public void setPetWeightRange(PetWeightRange petWeightRange) {
        this.petWeightRange = petWeightRange;
    }

    public SpaServiceDetailId getSpaServiceDetailId() {
        return spaServiceDetailId;
    }

    public void setSpaServiceDetailId(SpaServiceDetailId spaServiceDetailId) {
        this.spaServiceDetailId = spaServiceDetailId;
    }
}
