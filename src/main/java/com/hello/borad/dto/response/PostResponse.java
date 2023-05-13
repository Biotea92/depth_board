package com.hello.borad.dto.response;

import com.hello.borad.domain.board.entity.Post;

public record PostResponse(Long postId, String title, String content, CategoryResponse categoryResponse) {

    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), CategoryResponse.from(post.getCategory()));
    }
}
