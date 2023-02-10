package com.yht.exerciseassist.exceoption.controller;

import com.yht.exerciseassist.domain.diary.controller.DiaryController;
import com.yht.exerciseassist.exceoption.CommonExceptionHandler;
import com.yht.exerciseassist.exceoption.dto.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice(assignableTypes = DiaryController.class)
@RequiredArgsConstructor
public class DiaryExceptionController {

    private final CommonExceptionHandler commonExceptionHandler;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse validHandle(MethodArgumentNotValidException exception) {

        return commonExceptionHandler.exceptionArrayRes(exception, log);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse findMemberException(IllegalArgumentException exception) {

        return commonExceptionHandler.exceptionRes(exception, log);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ExceptionResponse exceptionHandle(IOException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ExceptionResponse exceptionHandle(HttpMediaTypeNotSupportedException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), exception.getMessage());
    }
}
