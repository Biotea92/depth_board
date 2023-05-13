package com.hello.borad.application.usecase.board;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.entity.Post;
import com.hello.borad.domain.board.service.CategoryReadService;
import com.hello.borad.domain.board.service.PostWriteService;
import com.hello.borad.dto.request.PostCreateRequest;
import com.hello.borad.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostUsecase {

    private final CategoryReadService categoryReadService;
    private final PostWriteService postWriteService;

    public PostResponse execute(Long categoryId, PostCreateRequest request) {
        Category category = categoryReadService.getCategory(categoryId);
        Post newPost = postWriteService.register(request.title(), request.content(), category);
        return PostResponse.from(newPost);
    }
}
