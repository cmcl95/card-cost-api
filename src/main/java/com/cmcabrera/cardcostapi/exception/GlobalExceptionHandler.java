package com.cmcabrera.cardcostapi.exception;

import com.cmcabrera.cardcostapi.dto.ApiErrorDTO;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                details
        );

        return new ResponseEntity<>(apiErrorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                Collections.singletonList(ex.getMessage())
        );
        return new ResponseEntity<>(apiErrorDTO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ApiErrorDTO apiErrorDTO = new ApiErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                Collections.singletonList(ex.getLocalizedMessage())
        );
        return new ResponseEntity<>(apiErrorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
