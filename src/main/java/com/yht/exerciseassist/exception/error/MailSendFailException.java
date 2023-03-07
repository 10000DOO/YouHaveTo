package com.yht.exerciseassist.exception.error;

import org.springframework.mail.MailException;

public class MailSendFailException extends MailException {
    public MailSendFailException(String msg) {
        super(msg);
    }
}
