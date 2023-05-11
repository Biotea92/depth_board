package com.hello.borad.dto.response;

import com.hello.borad.domain.board.entity.Category;

public record ChildCategoryResponse(
        Long categoryId,
        String title,
        int depth,
        int sequence,
        boolean hasPost
) {
    public static ChildCategoryResponse from(Category childCategory) {
        return new ChildCategoryResponse(
                childCategory.getId(),
                childCategory.getTitle(),
                childCategory.getDepth(),
                childCategory.getSequence(),
                childCategory.isHasPost()
        );
    }
}
