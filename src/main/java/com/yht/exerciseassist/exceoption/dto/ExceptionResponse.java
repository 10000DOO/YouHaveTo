package com.yht.exerciseassist.exceoption.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse<T> {

    private int status;
    private T error;
}