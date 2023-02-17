package com.liondevs.fastfood.authorizationserver.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;
    public UserNotFoundException(String email, HttpStatus httpStatus){
        super("Could not find user with email :  "+email);
        this.httpStatus = httpStatus;

    }
}
