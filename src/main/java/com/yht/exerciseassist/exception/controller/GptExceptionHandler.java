package com.yht.exerciseassist.exception.controller;

import com.yht.exerciseassist.exception.dto.ExceptionResponse;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.gpt.routine.controller.RoutineController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice(assignableTypes = RoutineController.class)
public class GptExceptionHandler {

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(HttpServerErrorException.class)
    public ExceptionResponse httpServerErrorExceptionHandle(IOException exception) {

        log.error("{} // {}", exception.getMessage(), ErrorCode.UNKNOWN_SERVER_ERROR.getMessage());
        return new ExceptionResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ErrorCode.UNKNOWN_SERVER_ERROR.getMessage());
    }
}
