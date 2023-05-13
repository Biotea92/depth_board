package com.hello.borad.domain.board.service;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.entity.Post;
import com.hello.borad.domain.board.repository.PostRepository;
import com.hello.borad.exception.badrequest.InvalidCategoryDepthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostWriteService {

    private final PostRepository postRepository;

    public Post register(String title, String content, Category category) {
        checkRightCategory(category);
        Post newPost = Post.create(title, content, category);
        return postRepository.save(newPost);
    }

    private void checkRightCategory(Category category) {
        if (!category.isDepthTwo()) {
            throw new InvalidCategoryDepthException();
        }
    }
}
