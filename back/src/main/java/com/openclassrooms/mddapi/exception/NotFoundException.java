package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {

	private final HttpStatus httpStatus;

	public NotFoundException(String message) {
		super(message + " non trouvé");
		this.httpStatus = HttpStatus.NOT_FOUND;
	}

	public String getMessage() {
		return super.getMessage();
	}
}

}
