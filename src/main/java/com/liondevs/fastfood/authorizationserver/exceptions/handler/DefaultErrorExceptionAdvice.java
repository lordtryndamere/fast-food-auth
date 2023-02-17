package com.liondevs.fastfood.authorizationserver.exceptions.handler;

import com.liondevs.fastfood.authorizationserver.exceptions.DefaultErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DefaultErrorExceptionAdvice {
    @ResponseBody
    @ExceptionHandler(DefaultErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<?> handleConflict(
            DefaultErrorException ex) {
        Map<String, String> bodyOfResponse = new HashMap<>();
        String httpStatusCode = Integer.toString(ex.getHttpStatus().value());
        bodyOfResponse.put("message",ex.getMessage());
        bodyOfResponse.put("code",httpStatusCode);
        return ResponseEntity.ok(bodyOfResponse) ;
    }
}
