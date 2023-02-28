package com.yht.exerciseassist.exceoption.controller;

import com.yht.exerciseassist.domain.member.controller.MemberController;
import com.yht.exerciseassist.exceoption.CommonExceptionHandler;
import com.yht.exerciseassist.exceoption.dto.ExceptionResponse;
import com.yht.exerciseassist.exceoption.error.AuthenticationException;
import com.yht.exerciseassist.exceoption.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
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

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ExceptionResponse passwordExcepHandle(BadCredentialsException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.UNAUTHORIZED.value());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ExceptionResponse idExcepHandle(InternalAuthenticationServiceException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.UNAUTHORIZED.value());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ExceptionResponse signInFailHandle(AuthenticationException exception) {

        return commonExceptionHandler.exceptionRes(exception, log, HttpStatus.UNAUTHORIZED.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PSQLException.class)
    public ExceptionResponse uniqueException(PSQLException exception) {
        if (exception.getMessage().contains("email")) {
            log.error(ErrorCode.NOT_FOUND_EMAIL.getMessage());
            return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ErrorCode.NOT_FOUND_EMAIL.getMessage());
        } else if (exception.getMessage().contains("username")) {
            log.error(ErrorCode.NOT_FOUND_USERNAME.getMessage());
            return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ErrorCode.NOT_FOUND_USERNAME.getMessage());
        } else {
            log.error(ErrorCode.NOT_FOUND_LOGIN_ID.getMessage());
            return new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ErrorCode.NOT_FOUND_LOGIN_ID.getMessage());
        }
    }
}
