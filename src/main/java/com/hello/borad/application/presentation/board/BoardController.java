package com.hello.borad.application.presentation.board;

import com.hello.borad.application.usecase.board.CreateCategoryUsecase;
import com.hello.borad.dto.request.CategoryCreateRequest;
import com.hello.borad.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final CreateCategoryUsecase createCategoryUsecase;

    @PostMapping("/category")
    public CategoryResponse createCategory(@RequestBody @Validated CategoryCreateRequest request) {
        return createCategoryUsecase.execute(request);
    }
}
