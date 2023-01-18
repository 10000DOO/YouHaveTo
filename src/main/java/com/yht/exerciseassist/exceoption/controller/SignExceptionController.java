package com.yht.exerciseassist.exceoption.controller;

import com.yht.exerciseassist.domain.member.controller.MemberController;
import com.yht.exerciseassist.exceoption.dto.ErrorMessageDto;
import com.yht.exerciseassist.exceoption.dto.ExceptionResponse;
import com.yht.exerciseassist.exceoption.error.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice(assignableTypes = MemberController.class)
public class SignExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse validHandle(MethodArgumentNotValidException exception) {

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse signUpHandle(IllegalArgumentException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ExceptionResponse signInHandle(BadCredentialsException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ExceptionResponse signInHandle(InternalAuthenticationServiceException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ExceptionResponse signInHandle(AuthenticationException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
