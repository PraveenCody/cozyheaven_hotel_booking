package com.springboot.cozyheaven.config;

import com.springboot.cozyheaven.dto.response.ErrorDto;
import com.springboot.cozyheaven.exceptions.OwnershipException;
import com.springboot.cozyheaven.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> resourceNotFoundHandler(ResourceNotFoundException e){

        return ResponseEntity.badRequest().body(
                new ErrorDto("Resource not found","Given data not found in base",Instant.now())
        );

    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> ownerShipException(OwnershipException w){

        return ResponseEntity.badRequest().body(
                new ErrorDto("Restricted Access","Auth user can acccess this data",Instant.now())
        );

    }



}
