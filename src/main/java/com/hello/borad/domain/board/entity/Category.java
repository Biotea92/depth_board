package com.hello.borad.domain.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    private int depth;

    private int sequence;

    private boolean hasPost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> childCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    private Category(String title, int depth, int sequence, Category parentCategory) {
        this.title = title;
        this.depth = depth;
        this.sequence = sequence;
        this.parentCategory = parentCategory;
        this.hasPost = false;
    }

    public static Category create(String title, int nextSequence) {
        return new Category(title, 1, nextSequence, null);
    }

    public static Category createWithParent(String title, Category parentCategory) {
        int nextSequence = parentCategory.getChildCategories().size() + 1;
        Category category = new Category(title, 2, nextSequence, parentCategory);
        parentCategory.childCategories.add(category);
        return category;
    }
}
