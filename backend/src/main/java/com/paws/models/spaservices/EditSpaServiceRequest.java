package com.paws.models.spaservices;

import com.paws.payloads.response.ServiceDetailDto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditSpaServiceRequest {
    private long id;

    @NotEmpty(message = "Tên không được rỗng.")
    private String name;

    private String description;

    @DecimalMin(message = "Giá tiền phải lớn hơn 1000 VND.", value = "1000")
    @NotNull(message = "Giá tiền không được rỗng")
    private BigDecimal defaultPrice;

    @DecimalMin(message = "Thời gian phải lớn hơn 1 phút.", value = "1.0")
    private float defaultEstimatedCompletionMinutes;

    List<ServiceDetailDto> details;
}
