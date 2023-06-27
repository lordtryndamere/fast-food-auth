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
    private String email;
    @NotEmpty(message = "Phone is required")
    @NotNull
    @NotBlank(message = "phone is mandatory")
    private String phone;

    @NotEmpty(message = "from is required")
    @NotNull
    @NotBlank(message = "from is mandatory")
    private String from;
    private Long placeId;
    @NotEmpty(message = "Password is required")
    @NotNull
    @NotBlank(message = "password is mandatory")
    private String password;


}
