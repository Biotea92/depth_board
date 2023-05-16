package com.hello.borad.dto.response;

import com.hello.borad.domain.board.entity.Category;

import java.util.List;

public record ChildCategoryResponse(
        Long categoryId,
        String title,
        int depth,
        int sequence,
        boolean hasPost,
        List<ChildCategoryResponse> childCategoryResponses
) {
    public static ChildCategoryResponse from(Category childCategory) {
        return new ChildCategoryResponse(
                childCategory.getId(),
                childCategory.getTitle(),
                childCategory.getDepth(),
                childCategory.getSequence(),
                childCategory.isHasPost(),
                List.of()
        );
    }
}
