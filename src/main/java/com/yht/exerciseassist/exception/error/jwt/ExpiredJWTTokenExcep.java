package com.yht.exerciseassist.exception.error.jwt;

public class ExpiredJWTTokenExcep extends RuntimeException {

    public ExpiredJWTTokenExcep(String message) {
        super(message);
    }
}
