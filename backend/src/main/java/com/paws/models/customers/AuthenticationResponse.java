package com.paws.models.customers;

import com.paws.entities.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private long id;
    private String username;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private Gender gender;
    private String token;
}
