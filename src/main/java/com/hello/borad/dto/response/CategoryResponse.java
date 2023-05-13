package com.hello.borad.dto.response;

import com.hello.borad.domain.board.entity.Category;

public record CategoryResponse(Long categoryId, String title, int depth, int sequence) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getTitle(), category.getDepth(), category.getSequence());
    }
}
