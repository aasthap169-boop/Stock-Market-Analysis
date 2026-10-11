package com.stockmarket.analysis.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.stockmarket.analysis.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseStructure<String>> handleBadRequest(BadRequestException exception) {

        ResponseStructure<String> response = new ResponseStructure<String>();

        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        response.setData(null);

        return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseStructure<String>> handleNotFound(ResourceNotFoundException exception) {

        ResponseStructure<String> response = new ResponseStructure<String>();

        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setData(null);

        return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseStructure<String>> handleValidation(MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining(", "));

        ResponseStructure<String> response = new ResponseStructure<String>();

        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(message);
        response.setData(null);

        return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseStructure<String>> handleGeneralException(Exception exception) {

        exception.printStackTrace();

        ResponseStructure<String> response = new ResponseStructure<String>();

        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(exception.getMessage());
        response.setData(null);

        return new ResponseEntity<ResponseStructure<String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}