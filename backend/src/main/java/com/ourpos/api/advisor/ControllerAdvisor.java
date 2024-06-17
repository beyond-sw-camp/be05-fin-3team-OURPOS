package com.ourpos.api.advisor;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ourpos.api.Result;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler
    public Result<Void> handleException(BindException e) {
        return new Result<>(HttpStatus.BAD_REQUEST.value(),
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), null);
    }

    @ExceptionHandler
    public Result<Void> handleException(IllegalArgumentException e) {
        return new Result<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    @ExceptionHandler
    public Result<Void> handleException(IllegalStateException e) {
        return new Result<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }
}
