package com.paws.payloads.response;

import com.paws.entities.common.enums.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private Long id;
    private BigDecimal totalAmount;
    private BillStatus status;
    private List<BillItemDto> billItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
