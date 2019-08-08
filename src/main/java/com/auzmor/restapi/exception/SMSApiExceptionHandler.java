package com.auzmor.restapi.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.auzmor.restapi.common.AppConstants.*;
import com.auzmor.restapi.dto.ApiResponse;

@RestController
@ControllerAdvice
public class SMSApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
		ApiResponse response = new ApiResponse(null, fieldErrors.toString());
		return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(ParameterNotFoundException.class)
	public final ResponseEntity<ApiResponse> handleParameterNotFoundException(ParameterNotFoundException ex, WebRequest request) {
	  return new ResponseEntity<>(new ApiResponse(null, messageSource.getMessage(MSG_KEY_ERROR_PARAM_NOTFOUND, new String[] {ex.getParameter()}, 
			  LocaleContextHolder.getLocale())), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ApiResponse> handleAllExceptions(Exception ex, WebRequest request) {
	  return new ResponseEntity<>(new ApiResponse(null, messageSource.getMessage(MSG_KEY_ERROR_UNKNOWN, null, LocaleContextHolder.getLocale())), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
