package com.liondevs.fastfood.authorizationserver.service;

import com.liondevs.fastfood.authorizationserver.auth.dto.AuthenticationRequest;
import com.liondevs.fastfood.authorizationserver.auth.dto.AuthenticationResponse;
import com.liondevs.fastfood.authorizationserver.auth.dto.RegisterRequest;

import com.liondevs.fastfood.authorizationserver.exceptions.DefaultErrorException;
import com.liondevs.fastfood.authorizationserver.exceptions.UserNotFoundException;
import com.liondevs.fastfood.authorizationserver.mapper.CreateUserInDTOToUser;
import com.liondevs.fastfood.authorizationserver.persistence.entity.User;
import com.liondevs.fastfood.authorizationserver.persistence.enums.Role;
import com.liondevs.fastfood.authorizationserver.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final CreateUserInDTOToUser mapper;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request){
    try{
        User user = mapper.map(request);
        boolean exist = userRepository.findByEmail(user.getEmail()).isPresent();
        if(exist){
            throw new DefaultErrorException("User email already exists", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
    catch (RuntimeException e){
            throw new DefaultErrorException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        try{
            boolean exists = userRepository.findByEmail(request.getEmail()).isPresent();
            if(!exists) throw new UserNotFoundException(request.getEmail(),HttpStatus.NOT_FOUND);
            //call authentication manager and authenticate
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail()).get();
            String token = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(token)
                    .build();
        }
        catch (RuntimeException e){
            throw  new DefaultErrorException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }



}
