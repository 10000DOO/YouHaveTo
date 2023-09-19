package com.yht.exerciseassist.exception;

import com.yht.exerciseassist.exception.dto.ErrorMessageDto;
import com.yht.exerciseassist.exception.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonExceptionHandler {

    public ExceptionResponse exceptionArrayRes(BindException exception, Logger log) {
        List<ErrorMessageDto> exceptionResponses = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            ErrorMessageDto errorMessageDto = new ErrorMessageDto();
            errorMessageDto.setError(fieldError.getDefaultMessage());
            log.error(fieldError.getDefaultMessage());
            exceptionResponses.add(errorMessageDto);
        }
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exceptionResponses);
    }

    public ExceptionResponse exceptionRes(Exception exception, Logger log, int statusCode) {

        List<ErrorMessageDto> exceptionResponses = new ArrayList<>();
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(exception.getMessage());
        exceptionResponses.add(errorMessageDto);

        log.error(exception.getMessage());
        return new ExceptionResponse(statusCode, exceptionResponses);
    }

    public ExceptionResponse customExceptionRes(Exception exception, Logger log, int statusCode, String message) {

        List<ErrorMessageDto> exceptionResponses = new ArrayList<>();
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(message);
        exceptionResponses.add(errorMessageDto);

        log.error("{} // {}", exception.getMessage(), message);
        return new ExceptionResponse(statusCode, exceptionResponses);
    }
}
