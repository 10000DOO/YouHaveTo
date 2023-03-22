package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.emailCode.EmailCode;

public class EmailCodeFactory {

    public static EmailCode createEmailCode() {
        return new EmailCode("test@test.com", "123456789ABC");
    }
}
