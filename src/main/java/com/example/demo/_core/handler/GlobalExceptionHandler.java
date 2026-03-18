package com.example.demo._core.handler;

import com.example.demo._core.handler.ex.*;
import com.example.demo._core.utils.Resp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception400.class)
    public @ResponseBody ResponseEntity<?> handle400(Exception400 e) {
        return Resp.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception401.class)
    public @ResponseBody ResponseEntity<?> handle401(Exception401 e) {
        return Resp.fail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(Exception403.class)
    public @ResponseBody ResponseEntity<?> handle403(Exception403 e) {
        return Resp.fail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(Exception404.class)
    public @ResponseBody ResponseEntity<?> handle404(Exception404 e) {
        return Resp.fail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception500.class)
    public @ResponseBody ResponseEntity<?> handle500(Exception500 e) {
        return Resp.fail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
