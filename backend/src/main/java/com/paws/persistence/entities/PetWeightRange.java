package com.paws.persistence.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "pet_weight_ranges")
public class PetWeightRange {
    @EmbeddedId
    private PetWeightRangeId id;

    private String description;

    @Embeddable
    public static class PetWeightRangeId implements Serializable {
        @Column(name = "min_weight", precision = 4, scale = 2)
        private BigDecimal minWeight;

        @Column(name = "max_weight", precision = 4, scale = 2)
        private BigDecimal maxWeight;

        public PetWeightRangeId() {
        }

        public BigDecimal getMinWeight() {
            return minWeight;
        }

        public void setMinWeight(BigDecimal minWeight) {
            this.minWeight = minWeight;
        }

        public BigDecimal getMaxWeight() {
            return maxWeight;
        }

        public void setMaxWeight(BigDecimal maxWeight) {
            this.maxWeight = maxWeight;
        }
    }

    public PetWeightRange() {
    }

    public PetWeightRangeId getId() {
        return id;
    }

    public void setId(PetWeightRangeId id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}