package com.yht.exerciseassist.exceoption.error.jwt;

public class ExpiredJWTTokenExcep extends RuntimeException {

    public ExpiredJWTTokenExcep(String message) {
        super(message);
    }
}
