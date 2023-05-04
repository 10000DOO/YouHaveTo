package com.yht.exerciseassist.exception.controller;

import com.yht.exerciseassist.admin.post.controller.AdminPostController;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import com.yht.exerciseassist.exception.dto.ExceptionResponse;
import com.yht.exerciseassist.exception.error.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Slf4j
@RestControllerAdvice(assignableTypes = AdminPostController.class)
@RequiredArgsConstructor
public class AdminPostExceptionController {
    private final CommonExceptionHandler commonExceptionHandler;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ExceptionResponse ioExceptionHandle(IOException exception) {

        return commonExceptionHandler.customExceptionRes(exception, log, HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.IO_FAIL_EXCEOPTION.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionResponse postNotFound(EntityNotFoundException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalAccessException.class)
    public ExceptionResponse notYourPost(IllegalAccessException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.FORBIDDEN.value());
    }
}
