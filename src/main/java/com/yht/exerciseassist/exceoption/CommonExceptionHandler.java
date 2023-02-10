package com.yht.exerciseassist.exceoption;

import com.yht.exerciseassist.exceoption.dto.ErrorMessageDto;
import com.yht.exerciseassist.exceoption.dto.ExceptionResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonExceptionHandler {

    public ExceptionResponse exceptionArrayRes(MethodArgumentNotValidException exception, Logger log) {
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

    public ExceptionResponse exceptionRes(IllegalArgumentException exception, Logger log) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
