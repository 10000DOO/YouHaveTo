package com.yht.exerciseassist;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseResult<T>{
    private int status;
    private T data;
}

