package com.hello.borad.application.presentation.board;

import com.hello.borad.application.usecase.board.*;
import com.hello.borad.dto.request.CategoryCreateRequest;
import com.hello.borad.dto.request.CategoryEditRequest;
import com.hello.borad.dto.request.PostCreateRequest;
import com.hello.borad.dto.response.CategoryResponse;
import com.hello.borad.dto.response.ParentCategoryResponse;
import com.hello.borad.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final CreateCategoryUsecase createCategoryUsecase;
    private final GetCategoriesUsecase getCategoriesUsecase;
    private final EditCategoriesUsecase editCategoriesUsecase;
    private final CreatePostUsecase createPostUsecase;
    private final GetPostsUsecase getPostsUsecase;

    @PostMapping("/category")
    public CategoryResponse createCategory(@RequestBody @Validated CategoryCreateRequest request) {
        return createCategoryUsecase.execute(request);
    }

    @GetMapping("/category")
    public List<ParentCategoryResponse> getCategories() {
        return getCategoriesUsecase.execute();
    }

    @PutMapping("/category")
    public List<ParentCategoryResponse> editCategories(@RequestBody @Validated CategoryEditRequest request) {
        return editCategoriesUsecase.execute(request);
    }

    @PostMapping("/category/{categoryId}/post")
    public PostResponse createPost(@PathVariable Long categoryId, @RequestBody @Validated PostCreateRequest request) {
        return createPostUsecase.execute(categoryId, request);
    }

    @GetMapping("/category/{categoryId}/posts")
    public List<PostResponse> getPosts(@PathVariable Long categoryId) {
        return getPostsUsecase.execute(categoryId);
    }
}
