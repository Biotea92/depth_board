package com.hello.borad.dto.response;

import com.hello.borad.domain.board.entity.Category;

import java.util.List;

public record ParentCategoryResponse(
        Long categoryId,
        String title,
        int depth,
        int sequence,
        boolean hasPost,
        List<ChildCategoryResponse> childCategoryResponses
) {
    public static ParentCategoryResponse from(Category parentCategory) {
        return new ParentCategoryResponse(
                parentCategory.getId(),
                parentCategory.getTitle(),
                parentCategory.getDepth(),
                parentCategory.getSequence(),
                parentCategory.isHasPost(),
                parentCategory.getChildCategories().stream().map(ChildCategoryResponse::from).toList()
        );
    }
}
