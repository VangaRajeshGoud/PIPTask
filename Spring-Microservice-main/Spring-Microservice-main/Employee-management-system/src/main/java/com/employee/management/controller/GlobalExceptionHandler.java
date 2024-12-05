package com.employee.management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.employee.management.exception.DuplicateException;
import com.employee.management.model.ApiResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<ApiResponse> handleDuplicateException(DuplicateException ex) {
		ApiResponse response = new ApiResponse(ex.getMessage(), false);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
		ApiResponse response = new ApiResponse("An unexpected error occurred.", false);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
