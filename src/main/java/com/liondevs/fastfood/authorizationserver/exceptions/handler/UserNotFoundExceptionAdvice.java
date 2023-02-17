package com.liondevs.fastfood.authorizationserver.exceptions.handler;

import com.liondevs.fastfood.authorizationserver.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class UserNotFoundExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String,String> restaurantNotFoundHandler(UserNotFoundException ex) {
        Map<String, String> bodyOfResponse = new HashMap<>();
        String httpStatusCode = Integer.toString(HttpStatus.NOT_FOUND.value());
        bodyOfResponse.put("message",ex.getMessage());
        bodyOfResponse.put("code",httpStatusCode);
        return bodyOfResponse;
    }
        }
