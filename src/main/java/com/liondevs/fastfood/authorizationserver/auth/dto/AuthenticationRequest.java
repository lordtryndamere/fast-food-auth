package com.liondevs.fastfood.authorizationserver.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {


    @NotEmpty(message = "Email is required")
    @Email(message = "invalid email")
    @NotNull
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "Password is required")
    @NotNull
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotEmpty(message = "from is required")
    @NotNull
    @NotBlank(message = "from is mandatory")
    private String from;



}
