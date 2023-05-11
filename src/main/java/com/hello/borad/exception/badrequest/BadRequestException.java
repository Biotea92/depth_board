package com.hello.borad.exception.badrequest;

import com.hello.borad.exception.CustomException;

public class BadRequestException extends CustomException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    private static final int STATUS_CODE = 400;

    public BadRequestException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return STATUS_CODE;
    }
}
