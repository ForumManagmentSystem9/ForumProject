package com.example.demo.models.userfolder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;

public class UserLoginDTO {
    @NotNull
    @Unique
    @Email
    private String email;
    @NotNull
    @Size(min = 4, max = 32,
            message = "Password should be between 4 and 32 symbols")
    private String password;
    public UserLoginDTO(){}

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
