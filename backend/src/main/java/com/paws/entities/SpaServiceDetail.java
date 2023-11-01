package com.paws.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "spa_service_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @NoArgsConstructor
    @Data
    public static class SpaServiceDetailId implements Serializable {
        @Column(name = "spa_service_id")
        private Long spaServiceId;
        private PetWeightRange.PetWeightRangeId petWeightRangeId;
    }
}
