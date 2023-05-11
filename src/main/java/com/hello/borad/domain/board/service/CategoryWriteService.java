package com.hello.borad.domain.board.service;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.repository.CategoryRepository;
import com.hello.borad.exception.notfound.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryWriteService {

    private final CategoryRepository categoryRepository;
    private static final int DEPTH_ONE = 1;

    public Category register(String title) {
        int nextSequence = (int) categoryRepository.countByDepth(DEPTH_ONE) + 1;
        Category newCategory = Category.create(title, nextSequence);
        return categoryRepository.save(newCategory);
    }

    public Category register(String title, Long parentCategoryId) {
        Category parentCategory = getCategory(parentCategoryId);
        Category newCategory = Category.createWithParent(title, parentCategory);
        return categoryRepository.save(newCategory);
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findByIdWithChildren(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
    }
}
