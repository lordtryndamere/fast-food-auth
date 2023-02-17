package com.liondevs.fastfood.authorizationserver.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "firstName is required")
    @NotNull
    @NotBlank(message = "firstName is mandatory")
    private  String firstName;
    @NotEmpty(message = "Lastname is required")
    @NotNull
    @NotBlank(message = "lastName is mandatory")
    private String lastName;
    @NotEmpty(message = "Email is required")
    //TODO: AVERIGUAR EMAIL VALIDO
    @Email(message = "invalid email")
    @NotNull
    @NotBlank(message = "email is mandatory")
    private String email;
    @NotEmpty(message = "Phone is required")
    @NotNull
    @NotBlank(message = "phone is mandatory")
    private String phone;
    @NotEmpty(message = "Password is required")
    @NotNull
    @NotBlank(message = "password is mandatory")

    private String password;


}
