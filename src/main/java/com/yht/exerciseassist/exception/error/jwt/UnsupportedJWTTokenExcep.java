package com.yht.exerciseassist.exception.error.jwt;

public class UnsupportedJWTTokenExcep extends RuntimeException {

    public UnsupportedJWTTokenExcep(String message) {
        super(message);
    }
}
