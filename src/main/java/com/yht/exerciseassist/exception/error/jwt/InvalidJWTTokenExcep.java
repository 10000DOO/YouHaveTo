package com.yht.exerciseassist.exception.error.jwt;

public class InvalidJWTTokenExcep extends RuntimeException {

    public InvalidJWTTokenExcep(String message) {
        super(message);
    }
}
