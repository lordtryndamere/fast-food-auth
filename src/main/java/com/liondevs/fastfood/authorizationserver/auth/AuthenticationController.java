package com.liondevs.fastfood.authorizationserver.auth;

import com.liondevs.fastfood.authorizationserver.auth.dto.AuthenticationRequest;
import com.liondevs.fastfood.authorizationserver.auth.dto.AuthenticationResponse;
import com.liondevs.fastfood.authorizationserver.auth.dto.RegisterRequest;
import com.liondevs.fastfood.authorizationserver.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
         @Valid @RequestBody RegisterRequest request
    ){
    return  ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
           @Valid @RequestBody AuthenticationRequest request
    ){
        return  ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/validate-token")
    @ResponseBody
    public ResponseEntity<?> validateToken(
             @RequestParam(name = "token") String  token
    ){
        return  authenticationService.validateToken(token);
    }


}
