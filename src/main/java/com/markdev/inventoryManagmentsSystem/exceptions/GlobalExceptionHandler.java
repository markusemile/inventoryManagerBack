package com.markdev.inventoryManagmentsSystem.exceptions;

import com.markdev.inventoryManagmentsSystem.dto.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAllExceptions(Exception ex){
        Response response = Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleNotFoundException(NotFoundException ex){
        Response response = Response.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameValueRequiredException.class)
    public ResponseEntity<Response> handleNameValueRequiredException(NameValueRequiredException ex){
        Response response = Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Response> handleInvalidCredentialsExceptionException(InvalidCredentialsException ex){
        Response response = Response.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,Object>>  handleMessageNotReadableException(HttpMessageNotReadableException ex){
        Map<String, Object> errorResponse = new HashMap<>();

        String userFriendlyMessage = "Invalid or missing request body";

        if (ex.getMessage().contains("Required request body is missing")) {
            userFriendlyMessage = "Request body is missing";
        }

        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("message", userFriendlyMessage);

        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(DuplicateValueException.class)
    public ResponseEntity<Map<String,Object>> handleDuplicateValue(DuplicateValueException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status",HttpStatus.CONFLICT.value());
        errorResponse.put("message",ex.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status",HttpStatus.CONFLICT.value());
        errorResponse.put("message","Resource in use by other element ! Impossible to delete this resource.");
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }
}
