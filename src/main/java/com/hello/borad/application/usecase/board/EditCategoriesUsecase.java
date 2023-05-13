package com.hello.borad.application.usecase.board;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.service.CategoryWriteService;
import com.hello.borad.dto.request.CategoryEditRequest;
import com.hello.borad.dto.request.CategoryEditRequest.ParentCategoryEditRequest;
import com.hello.borad.dto.response.ParentCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class EditCategoriesUsecase {

    private final CategoryWriteService categoryWriteService;

    @Transactional
    public List<ParentCategoryResponse> execute(CategoryEditRequest request) {
        LinkedList<Category> categories = new LinkedList<>();
        registerOrUpdateCategories(request.parentCategories(), categories);
        removeCategories(request.removedCategoryIds());
        return categories.stream()
                .map(ParentCategoryResponse::from)
                .toList();
    }

    private void registerOrUpdateCategories(List<ParentCategoryEditRequest> parents, LinkedList<Category> categories) {
        AtomicInteger parentSequence = new AtomicInteger(0);
        parents.forEach(parent -> {
            Category parentCategory = registerOrUpdateParent(parentSequence, parent);
            categories.add(parentCategory);
            registerOrUpdateChild(parent, parentCategory);
        });
    }

    private Category registerOrUpdateParent(AtomicInteger parentSequence, ParentCategoryEditRequest parent) {
        return categoryWriteService.registerOrUpdateDepthOneCategory(
                parent.categoryId(), parent.title(), parentSequence.incrementAndGet()
        );
    }

    private void registerOrUpdateChild(ParentCategoryEditRequest parent, Category parentCategory) {
        AtomicInteger childSequence = new AtomicInteger(0);
        parent.childCategories()
                .forEach(child -> categoryWriteService.registerOrUpdateDepthTwoCategory(
                        child.categoryId(), child.title(), childSequence.incrementAndGet(), parentCategory.getId()
                ));
    }

    private void removeCategories(List<Long> categoryIds) {
        if (categoryIds.size() > 0) {
            categoryWriteService.deleteCategories(categoryIds);
        }
    }
}
