package com.hello.borad.application.usecase.board;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.entity.Post;
import com.hello.borad.domain.board.service.CategoryReadService;
import com.hello.borad.domain.board.service.PostReadService;
import com.hello.borad.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPostsUsecase {

    private final CategoryReadService categoryReadService;
    private final PostReadService postReadService;

    public List<PostResponse> execute(Long categoryId) {
        Category category = categoryReadService.getCategory(categoryId);
        List<Post> posts = postReadService.getPosts(category);
        return posts.stream()
                .map(PostResponse::from)
                .toList();
    }
}
