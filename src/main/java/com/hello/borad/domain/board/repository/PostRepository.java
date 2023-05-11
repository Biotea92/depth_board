package com.hello.borad.domain.board.repository;

import com.hello.borad.domain.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
