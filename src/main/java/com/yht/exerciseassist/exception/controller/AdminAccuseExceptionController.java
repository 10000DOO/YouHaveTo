package com.yht.exerciseassist.exception.controller;

import com.yht.exerciseassist.admin.accuse.controller.AdminAccuseController;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import com.yht.exerciseassist.exception.dto.ExceptionResponse;
import com.yht.exerciseassist.exception.error.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@RestControllerAdvice(assignableTypes = AdminAccuseController.class)
@RequiredArgsConstructor
public class AdminAccuseExceptionController {

    private final CommonExceptionHandler commonExceptionHandler;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionResponse accuseNotFound(EntityNotFoundException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.BAD_REQUEST.value());
    }
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalAccessException.class)
    public ExceptionResponse forbiddenHandle(IllegalAccessException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.FORBIDDEN.value());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ParseException.class)
    public ExceptionResponse parseExceptionHandle(ParseException exception) {

        return commonExceptionHandler.customExceptionRes(exception, log, HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.DATE_FORMAT_EXCEPTION.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ExceptionResponse ioExceptionHandle(IOException exception) {

        return commonExceptionHandler.customExceptionRes(exception, log, HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.IO_FAIL_EXCEOPTION.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse worngArgsException(IllegalArgumentException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.BAD_REQUEST.value());
    }
}
