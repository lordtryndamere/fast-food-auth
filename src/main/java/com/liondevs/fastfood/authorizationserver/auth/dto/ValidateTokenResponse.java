package com.liondevs.fastfood.authorizationserver.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTokenResponse {
    private String code;
    private String email;
    private Long idUser;
    private boolean isValidToken;

}
