package com.hello.borad.domain.board.service;

import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryReadService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findDepthOneWithChildrenOrderBySequence();
    }
}
