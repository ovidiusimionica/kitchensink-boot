package com.example.kitchensinkboot.web;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleUserNotFoundException(ConstraintViolationException ex,
                                                       WebRequest request) {

    Map<String, String> responseObj = new HashMap<>();

    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
    }

    return new ResponseEntity<>(responseObj, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handle(Exception ex, WebRequest request) {
    Map<String, String> responseObj = new HashMap<>();
    responseObj.put("error", ex.getMessage());
    return new ResponseEntity<>(responseObj, HttpStatus.BAD_REQUEST);
  }

}
