package com.yht.exerciseassist.exception.controller;

import com.yht.exerciseassist.domain.comment.controller.CommentController;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import com.yht.exerciseassist.exception.dto.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = CommentController.class)
@RequiredArgsConstructor
public class CommentExceptionController {

    private final CommonExceptionHandler commonExceptionHandler;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse validHandle(MethodArgumentNotValidException exception) {

        return commonExceptionHandler.exceptionArrayRes(exception, log);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ExceptionResponse NotFoundEntity(EntityNotFoundException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.BAD_REQUEST.value());
    }
}
