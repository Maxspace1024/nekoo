package com.brian.nekoo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GloabalErrorHandler extends Exception {
    @ExceptionHandler
    public ResponseEntity<Object> exceptionHandler(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
