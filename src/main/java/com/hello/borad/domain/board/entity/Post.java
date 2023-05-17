package com.hello.borad.domain.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    private Post(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
        category.updateHasPost();
    }

    public static Post create(String title, String content, Category category) {
        return new Post(title, content, category);
    }
}
