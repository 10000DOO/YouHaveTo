package com.yht.exerciseassist.exception.controller;

import com.yht.exerciseassist.exception.CommonExceptionHandler;
import com.yht.exerciseassist.exception.dto.ExceptionResponse;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.gpt.routine.controller.RoutineController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;

@Slf4j
@RestControllerAdvice(assignableTypes = RoutineController.class)
@RequiredArgsConstructor
public class GptExceptionHandler {

    private final CommonExceptionHandler commonExceptionHandler;

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(HttpServerErrorException.class)
    public ExceptionResponse httpServerErrorExceptionHandle(IOException exception) {

        return commonExceptionHandler.customExceptionRes(exception, log, HttpStatus.SERVICE_UNAVAILABLE.value(), ErrorCode.UNKNOWN_SERVER_ERROR.getMessage());
    }
}
