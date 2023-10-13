package com.paws.persistence.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "pet_weight_ranges")
public class PetWeightRange {
    @EmbeddedId
    private PetWeightRangeId id;

    private String description;

    @Embeddable
    public static class PetWeightRangeId implements Serializable {
        private String minWeight;

        private String maxWeight;

        public PetWeightRangeId() {
        }
    }
}