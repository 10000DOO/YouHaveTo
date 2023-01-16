package com.yht.exerciseassist.exceoption.error.jwt;

public class EmptyJWTTokenExcep extends RuntimeException{

    public EmptyJWTTokenExcep(String message) {
        super(message);
    }
}
