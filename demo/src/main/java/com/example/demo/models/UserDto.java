package com.example.demo.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;

public class UserDto {
    @NotNull
    @Size(min = 4, max = 32,
            message = "Username should be between 4 and 32 symbols")
    private String username;

    @NotNull
    @Size(min = 4, max = 32,
            message = "Password should be between 4 and 32 symbols")
    private String password;

    @NotNull
    @Size(min = 4, max = 32,
            message = "First name should be between 4 and 32 symbols")
    private String firstName;

    @NotNull
    @Size(min = 4, max = 32,
            message = "Last name should be between 4 and 32 symbols")
    private String lastName;

    @NotNull
    @Unique
    @Email
    private String email;

    private String phone;

    public UserDto(){}

    public String getUsername(){
        return username;
    }

    public void setUsername() {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lasrName) {
        this.lastName = lasrName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
