package com.hello.borad.domain.board.service;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.entity.Post;
import com.hello.borad.domain.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostReadService {

    private final PostRepository postRepository;

    public List<Post> getPosts(Category category) {
        return postRepository.findByCategory(category);
    }
}
