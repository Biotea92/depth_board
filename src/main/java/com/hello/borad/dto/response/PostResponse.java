package com.hello.borad.dto.response;

import com.hello.borad.domain.board.entity.Post;

import java.time.format.DateTimeFormatter;

public record PostResponse(
        Long postId,
        String title,
        String content,
        String createdAt,
        CategoryResponse categoryResponse) {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt().format(DATE_FORMAT),
                CategoryResponse.from(post.getCategory()));
    }
}
