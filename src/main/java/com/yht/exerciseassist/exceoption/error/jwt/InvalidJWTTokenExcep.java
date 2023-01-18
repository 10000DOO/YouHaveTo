package com.yht.exerciseassist.exceoption.error.jwt;

public class InvalidJWTTokenExcep extends RuntimeException {

    public InvalidJWTTokenExcep(String message) {
        super(message);
    }
}
