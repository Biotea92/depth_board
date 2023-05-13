package com.hello.borad.domain.board.repository;

import com.hello.borad.domain.board.entity.Category;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

import static com.hello.borad.domain.board.entity.QCategory.category;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Category> findDepthOneWithChildrenOrderBySequence() {
        List<Category> categories = jpaQueryFactory
                .selectFrom(category)
                .leftJoin(category.childCategories).fetchJoin()
                .where(depthOne())
                .orderBy(category.sequence.asc())
                .fetch();

        categories.forEach(category -> category.getChildCategories().sort(Comparator.comparing(Category::getSequence)));

        return categories;
    }

    private BooleanExpression depthOne() {
        return category.depth.eq(1);
    }
}
