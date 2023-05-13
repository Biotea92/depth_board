package com.hello.borad.exception.notfound;

public class CategoryNotFoundException extends NotFoundException {

    public CategoryNotFoundException() {
        addValidation("category", "category를 찾을 수 없습니다.");
    }
}
