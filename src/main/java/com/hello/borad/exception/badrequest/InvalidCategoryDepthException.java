package com.hello.borad.exception.badrequest;

public class InvalidCategoryDepthException extends BadRequestException {

    public InvalidCategoryDepthException() {
        addValidation("depth", "depth 2의 카테고리에만 글을 작성할 수 있습니다.");
    }
}
