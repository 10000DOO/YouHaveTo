package com.yht.exerciseassist.exception.error.jwt;

public class EmptyJWTTokenExcep extends RuntimeException {

    public EmptyJWTTokenExcep(String message) {
        super(message);
    }
}
