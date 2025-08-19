package com.sayem.trackfit.aiservice.service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sayem.trackfit.aiservice.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(RecommendationNotFoundException.class)
	public ResponseEntity<ErrorResponse> recommendationNotFoundExceptionHandler(RecommendationNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
				LocalDateTime.now(), 
				HttpStatus.BAD_REQUEST,
				"Bad Request",
				"Recommendation not found : " + ex.getMessage()
				);
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(errorResponse);
	}

}
