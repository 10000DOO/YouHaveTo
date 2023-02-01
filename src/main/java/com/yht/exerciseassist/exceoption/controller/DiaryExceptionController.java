package com.yht.exerciseassist.exceoption.controller;

import com.yht.exerciseassist.domain.diary.controller.DiaryController;
import com.yht.exerciseassist.exceoption.CustomExceptionHandler;
import com.yht.exerciseassist.exceoption.dto.ExceptionResponse;
import jakarta.validation.UnexpectedTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = DiaryController.class)
@RequiredArgsConstructor
public class DiaryExceptionController {

    private final CustomExceptionHandler customExceptionHandler;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse validHandle(MethodArgumentNotValidException exception) {

        return customExceptionHandler.exceptionArrayRes(exception, log);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse signUpHandle(IllegalArgumentException exception) {

        return customExceptionHandler.exceptionRes(exception, log);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnexpectedTypeException.class)
    public ExceptionResponse signUpHandle(UnexpectedTypeException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
