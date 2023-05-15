package com.liondevs.fastfood.authorizationserver.exceptions.handler;

import com.amazonaws.services.securityhub.model.InvalidAccessException;
import com.liondevs.fastfood.authorizationserver.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class InvalidRoleExceptionAdvice {
	@ResponseBody
	@ExceptionHandler(InvalidAccessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	Map<String,String> restaurantNotFoundHandler(UserNotFoundException ex) {
		Map<String, String> bodyOfResponse = new HashMap<>();
		String httpStatusCode = Integer.toString(HttpStatus.BAD_REQUEST.value());
		bodyOfResponse.put("message",ex.getMessage());
		bodyOfResponse.put("code",httpStatusCode);
		return bodyOfResponse;
	}
}
