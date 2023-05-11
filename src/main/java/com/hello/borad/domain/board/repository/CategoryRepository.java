package com.hello.borad.domain.board.repository;

import com.hello.borad.domain.board.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    long countByDepth(int depth);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.childCategories WHERE c.id = :id")
    Optional<Category> findByIdWithChildren(Long id);
}
