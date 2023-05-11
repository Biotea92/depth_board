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

    @OneToMany(mappedBy = "post", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
}