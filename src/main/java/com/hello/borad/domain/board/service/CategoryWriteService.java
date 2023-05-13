package com.hello.borad.domain.board.service;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.repository.CategoryRepository;
import com.hello.borad.exception.notfound.CategoryNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
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

    @Profile("!test")
    @PostConstruct
    public void initDataBind() {
        Category category1 = register("카테고리1");
        Category category2 = register("카테고리2");
        Category category3 = register("카테고리3");

        register("1", category1.getId());
        register("2", category1.getId());
        register("3", category1.getId());
        register("1", category2.getId());
        register("2", category2.getId());
        register("3", category2.getId());
        register("1", category3.getId());
        register("2", category3.getId());
        register("3", category3.getId());

    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findByIdWithChildren(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
    }
}
