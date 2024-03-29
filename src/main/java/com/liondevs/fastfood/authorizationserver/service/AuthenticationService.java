package com.liondevs.fastfood.authorizationserver.service;

import com.liondevs.fastfood.authorizationserver.auth.dto.AuthenticationRequest;
import com.liondevs.fastfood.authorizationserver.auth.dto.AuthenticationResponse;
import com.liondevs.fastfood.authorizationserver.auth.dto.RegisterRequest;

import com.liondevs.fastfood.authorizationserver.auth.dto.ValidateTokenResponse;
import com.liondevs.fastfood.authorizationserver.exceptions.DefaultErrorException;
import com.liondevs.fastfood.authorizationserver.exceptions.InvalidRoleException;
import com.liondevs.fastfood.authorizationserver.exceptions.UserNotFoundException;
import com.liondevs.fastfood.authorizationserver.mapper.CreateUserInDTOToUser;
import com.liondevs.fastfood.authorizationserver.persistence.entity.User;

import com.liondevs.fastfood.authorizationserver.persistence.enums.Role;
import com.liondevs.fastfood.authorizationserver.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.liondevs.fastfood.authorizationserver.constants.Constants;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final CreateUserInDTOToUser mapper;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private  final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request){
    try{
        User user = mapper.map(request);
        boolean exist = userRepository.findByEmail(user.getEmail()).isPresent();
        if(exist){
            throw new DefaultErrorException("User email: "+ user.getEmail()+" already exists", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
        return getAuthenticationResponse(user);
    }
    catch (RuntimeException e){
            throw new DefaultErrorException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

    public ValidateTokenResponse validateToken(String token){
    try {
        String userEmail = jwtService.getUserName(token);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new UserNotFoundException("User with email: " + userEmail+ " was not found",HttpStatus.NOT_FOUND));
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        boolean isValid =   jwtService.isTokenValid(token,userDetails);
        return ValidateTokenResponse.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .email(user.getEmail())
                .idUser(user.getId())
                .isValidToken(isValid)
                .build();
    } catch (UserNotFoundException e){
        throw  new UserNotFoundException(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (RuntimeException e){
        throw  new DefaultErrorException(e.getMessage(),HttpStatus.BAD_REQUEST);
    }


    }

    private AuthenticationResponse getAuthenticationResponse(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id",user.getId());
        userData.put("fullName",user.getFirstName() + " "+user.getLastName());
        userData.put("role",user.getRole());
        if(user.getPlaceId() != null) userData.put("placeId",user.getPlaceId());
        userData.put("email",user.getEmail());
        userData.put("phone",user.getPhone());
        String token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .userData(userData)
                .build();
    }

    private boolean validateRole(AuthenticationRequest request){
        User user =  userRepository.findByEmail(request.getEmail()).get();
        String from = request.getFrom();
        if(Objects.equals(from,Constants.APP_USER)){
            return Objects.equals(user.getRole(),Role.USER);
        }
        if(Objects.equals(from,Constants.APP_HANDLER_PLACES)){
            return Objects.equals(user.getRole(),Role.ADMIN_PLACE);
        }
        return  Objects.equals(user.getRole(),Role.SUPER_ADMIN);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        try{
            boolean exists = userRepository.findByEmail(request.getEmail()).isPresent();
            if(!exists) throw new UserNotFoundException(request.getEmail(),HttpStatus.NOT_FOUND);
            //call authentication manager and authenticate
            if(!validateRole(request)) throw new InvalidRoleException("Invalid Role, please verify it.", HttpStatus.BAD_REQUEST);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail()).get();
            return getAuthenticationResponse(user);
        }
        catch (RuntimeException e){
            throw  new DefaultErrorException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }



}
