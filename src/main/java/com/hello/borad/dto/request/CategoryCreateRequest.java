package com.hello.borad.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(@NotBlank String title, @Min(-1) Long parentCategoryId) {

    public CategoryCreateRequest(String title, Long parentCategoryId) {
        this.title = title;
        this.parentCategoryId = parentCategoryId == null ? -1L : parentCategoryId;
    }
}
