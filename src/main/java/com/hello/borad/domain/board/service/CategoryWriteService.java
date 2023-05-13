package com.hello.borad.domain.board.service;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.repository.CategoryRepository;
import com.hello.borad.exception.notfound.CategoryNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryWriteService {

    private final CategoryRepository categoryRepository;
    private static final int DEPTH_ONE = 1;
    private static final int DEPTH_TWO = 2;
    private static final Long UNREGISTERED_ID = -1L;

    public Category registerDepthOneCategory(String title) {
        int nextSequence = (int) categoryRepository.countByDepth(DEPTH_ONE) + 1;
        Category newCategory = Category.create(title, DEPTH_ONE, nextSequence);
        return categoryRepository.save(newCategory);
    }

    public Category registerDepthTwoCategory(String title, Long parentCategoryId) {
        Category parentCategory = getCategoryWithChildren(parentCategoryId);
        int nextSequence = parentCategory.getChildCategories().size() + 1;
        Category newCategory = Category.createWithParent(title, DEPTH_TWO, nextSequence, parentCategory);
        return categoryRepository.save(newCategory);
    }

    public Category registerOrUpdateDepthOneCategory(Long categoryId, String title, int sequence) {
        if (categoryId.equals(UNREGISTERED_ID)) {
            Category newCategory = Category.create(title, DEPTH_ONE, sequence);
            return categoryRepository.save(newCategory);
        }
        Category category = getCategory(categoryId);
        category.update(title, DEPTH_ONE, sequence);
        return category;
    }

    public Category registerOrUpdateDepthTwoCategory(Long categoryId, String title, int sequence, Long parentCategoryId) {
        Category parentCategory = getCategory(parentCategoryId);
        if (categoryId.equals(UNREGISTERED_ID)) {
            Category newCategory = Category.createWithParent(title, DEPTH_TWO, sequence, parentCategory);
            return categoryRepository.save(newCategory);
        }
        Category category = getCategory(categoryId);
        category.update(title, DEPTH_TWO, sequence, parentCategory);
        return category;
    }

    public void deleteCategories(List<Long> ids) {
        categoryRepository.deleteAllById(ids);
    }

    @PostConstruct
    public void initDataBind() {
        Category category1 = registerDepthOneCategory("카테고리1");
        Category category2 = registerDepthOneCategory("카테고리2");
        Category category3 = registerDepthOneCategory("카테고리3");

        registerDepthTwoCategory("1-1", category1.getId());
        registerDepthTwoCategory("1-2", category1.getId());
        registerDepthTwoCategory("1-3", category1.getId());
        registerDepthTwoCategory("2-1", category2.getId());
        registerDepthTwoCategory("2-2", category2.getId());
        registerDepthTwoCategory("2-3", category2.getId());
        registerDepthTwoCategory("3-1", category3.getId());
        registerDepthTwoCategory("3-2", category3.getId());
        registerDepthTwoCategory("3-3", category3.getId());
    }

    private Category getCategoryWithChildren(Long categoryId) {
        return categoryRepository.findByIdWithChildren(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
    }
}
