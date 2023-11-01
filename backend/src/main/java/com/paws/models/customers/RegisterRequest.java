package com.paws.models.customers;

import com.paws.entities.common.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Size(min = 8, message = "Tài khoản phải từ 8 kí tự trở lên.")
    private String username;


    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$" ,
            message = "Mật khẩu phải bao gồm 8 ký tự trở lên, 1 chữ in thường, 1 chữ in hoa, 1 số và 1 ký tự đặc biệt.")
    private String password;

    @Email(message = "Email không hợp lệ.")
    private String email;

    @NotEmpty(message = "Tên không được rỗng.")
    private String fullName;

    @NotEmpty(message = "Địa chỉ không được rỗng.")
    private String address;

    @NotEmpty(message = "Số điện thoại không được rỗng.")
    private String phoneNumber;

    @NotNull(message = "Giới tính không được rỗng.")
    private Gender gender;
}
