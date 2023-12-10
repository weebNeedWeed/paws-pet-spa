package com.paws.models.customers;

import com.paws.entities.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
