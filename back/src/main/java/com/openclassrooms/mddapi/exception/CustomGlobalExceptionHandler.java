package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

	@ExceptionHandler(exception = BadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(BadRequestException exception) {
		String bodyResponse = exception.getMessage();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bodyResponse);
	}

	@ExceptionHandler(exception = NotFoundException.class)
	public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
		String bodyResponse = exception.getMessage();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bodyResponse);
	}

}
