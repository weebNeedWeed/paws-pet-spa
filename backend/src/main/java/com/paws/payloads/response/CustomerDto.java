package com.paws.payloads.response;

import com.paws.entities.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
