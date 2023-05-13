package com.hello.borad.domain.board.repository;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategory(Category category);
}
