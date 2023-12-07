package com.paws.payloads.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class MakeAppointmentItemRequest {
    @NotEmpty(message = "Tên thú cưng không được rỗng.")
    private String petName;

    @NotNull(message = "Loại thú cưng không được rỗng.")
    private Long petTypeId;

    @NotEmpty(message = "Phải đăng ký ít nhất 1 dịch vụ.")
    private List<Long> serviceIds;

    public MakeAppointmentItemRequest() {
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Long getPetTypeId() {
        return petTypeId;
    }

    public void setPetTypeId(long petTypeId) {
        this.petTypeId = petTypeId;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<Long> serviceIds) {
        this.serviceIds = serviceIds;
    }
}
