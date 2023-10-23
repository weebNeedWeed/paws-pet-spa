package com.paws.services.customers.common;

import com.paws.entities.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CustomerDto {
    private long id;
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private Gender gender;
}
