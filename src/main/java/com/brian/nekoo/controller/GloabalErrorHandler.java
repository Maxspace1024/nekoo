package com.brian.nekoo.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GloabalErrorHandler extends Exception {

    @ExceptionHandler
    public ResponseEntity<Object> expiredJwtExceptionHandler(ExpiredJwtException exception) {
//        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler
    public ResponseEntity<Object> malformedJwtExceptionHandler(MalformedJwtException exception) {
//        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler
    public ResponseEntity<Object> exceptionHandler(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
