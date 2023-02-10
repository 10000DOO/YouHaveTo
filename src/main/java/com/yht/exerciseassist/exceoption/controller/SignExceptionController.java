package com.yht.exerciseassist.exceoption.controller;

import com.yht.exerciseassist.domain.member.controller.MemberController;
import com.yht.exerciseassist.exceoption.CommonExceptionHandler;
import com.yht.exerciseassist.exceoption.dto.ExceptionResponse;
import com.yht.exerciseassist.exceoption.error.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = MemberController.class)
@RequiredArgsConstructor
public class SignExceptionController {

    private final CommonExceptionHandler commonExceptionHandler;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse validHandle(MethodArgumentNotValidException exception) {

        return commonExceptionHandler.exceptionArrayRes(exception, log);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse signUpHandle(IllegalArgumentException exception) {

        return commonExceptionHandler.exceptionRes(exception, log);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public ExceptionResponse passwordExcepHandle(BadCredentialsException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ExceptionResponse idExcepHandle(InternalAuthenticationServiceException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ExceptionResponse signInFailHandle(AuthenticationException exception) {

        log.error(exception.getMessage());
        return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
