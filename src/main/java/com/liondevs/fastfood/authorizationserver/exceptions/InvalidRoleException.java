package com.liondevs.fastfood.authorizationserver.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRoleException extends  RuntimeException{
	private final String message;
	private final HttpStatus httpStatus;
	public InvalidRoleException(String message, HttpStatus httpStatus){
		super(message);
		this.message = message;
		this.httpStatus = httpStatus;

	}
}
