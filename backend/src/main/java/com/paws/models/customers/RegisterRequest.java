package com.paws.models.customers;

import com.paws.entities.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private Gender gender;
}
