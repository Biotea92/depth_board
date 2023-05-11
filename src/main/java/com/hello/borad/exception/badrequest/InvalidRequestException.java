package com.hello.borad.exception.badrequest;

public class InvalidRequestException extends BadRequestException {

    public InvalidRequestException(String fieldName, String message) {
        addValidation(fieldName, message);
    }
}
