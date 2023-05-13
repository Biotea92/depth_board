package com.hello.borad.application.usecase.board;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.service.CategoryWriteService;
import com.hello.borad.dto.request.CategoryCreateRequest;
import com.hello.borad.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUsecase {

    private final CategoryWriteService categoryWriteService;
    private static final Long NO_PARENT_CATEGORY_ID = -1L;

    public CategoryResponse execute(CategoryCreateRequest request) {
        if (request.parentCategoryId().equals(NO_PARENT_CATEGORY_ID)) {
            Category newCategory = categoryWriteService.registerDepthOneCategory(request.title());
            return CategoryResponse.from(newCategory);
        }
        Category newCategory = categoryWriteService.registerDepthTwoCategory(request.title(), request.parentCategoryId());
        return CategoryResponse.from(newCategory);
    }
}
