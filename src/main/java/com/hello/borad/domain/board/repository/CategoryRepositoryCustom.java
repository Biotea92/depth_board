package com.hello.borad.domain.board.repository;

import com.hello.borad.domain.board.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {

    List<Category> findDepthOneWithChildrenOrderBySequence();
}
