package com.yht.exerciseassist.exception.controller;

import com.yht.exerciseassist.domain.diary.controller.DiaryController;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import com.yht.exerciseassist.exception.dto.ExceptionResponse;
import com.yht.exerciseassist.exception.error.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
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

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ExceptionResponse ioExceptionHandle(IOException exception) {

        log.error("{} // {}", exception.getMessage(), ErrorCode.IO_FAIL_EXCEOPTION.getMessage());
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.IO_FAIL_EXCEOPTION.getMessage());
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ExceptionResponse wrongContentTpye(HttpMediaTypeNotSupportedException exception) {

        log.error("{} // {}", exception.getMessage(), ErrorCode.WRONG_CONTENT_TYPE.getMessage());
        return new ExceptionResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ErrorCode.WRONG_CONTENT_TYPE.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionResponse diaryNotFound(EntityNotFoundException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.BAD_REQUEST.value());
    }
}
