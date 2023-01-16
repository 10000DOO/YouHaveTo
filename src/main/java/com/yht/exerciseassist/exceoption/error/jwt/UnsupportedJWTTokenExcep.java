package com.yht.exerciseassist.exceoption.error.jwt;

public class UnsupportedJWTTokenExcep extends RuntimeException{

    public UnsupportedJWTTokenExcep(String message){
        super(message);
    }
}
