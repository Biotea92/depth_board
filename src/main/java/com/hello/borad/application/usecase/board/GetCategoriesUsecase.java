package com.hello.borad.application.usecase.board;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.service.CategoryReadService;
import com.hello.borad.dto.response.ParentCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCategoriesUsecase {

    private final CategoryReadService categoryReadService;

    public List<ParentCategoryResponse> execute() {
        List<Category> categories = categoryReadService.getCategories();

        return categories.stream()
                .map(ParentCategoryResponse::from)
                .toList();
    }
}
