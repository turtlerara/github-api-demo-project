package com.demo.app.api.controller;

import static java.util.Optional.ofNullable;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
    final String errorMessage =
        ofNullable(ex.getConstraintViolations()).orElseGet(Set::of).stream()
            .findFirst()
            .map(ConstraintViolation::getMessage)
            .orElseGet(ex::getMessage);

    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
  }
}
