package com.example.demo.models.userfolder;

import jakarta.validation.constraints.NotEmpty;

public class RegistreDto extends LoginDto {
    @NotEmpty(message = "Username required")
    private String username;

    @NotEmpty(message = "Password confirmation can't be empty")
    private String passwordConfirm;

    @NotEmpty(message = "First name can't be empty")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty")
    private String lastName;

    @NotEmpty(message = "Email can't be empty")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "Username required") String username) {
        this.username = username;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
