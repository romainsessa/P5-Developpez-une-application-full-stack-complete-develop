package com.openclassrooms.mddapi.exception;

public class BadRequestException extends RuntimeException {
	
	public BadRequestException(String message) {
		super(message);
	}

	public String getMessage() {
		return super.getMessage();
	}
}
