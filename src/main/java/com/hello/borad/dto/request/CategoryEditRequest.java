package com.hello.borad.dto.request;

import com.hello.borad.dto.request.validator.NullOrEmpty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record CategoryEditRequest(@Valid List<ParentCategoryEditRequest> parentCategories, List<Long> removedCategoryIds) {

        public record ParentCategoryEditRequest(
                @Min(-1L)
                @Max(9999999999L)
                Long categoryId,

                @NotBlank
                String title,

                @Valid
                List<ChildCategoryEditRequest> childCategories
        ) {
                public ParentCategoryEditRequest(Long categoryId, String title, List<ChildCategoryEditRequest> childCategories) {
                        this.categoryId = categoryId == null ? -1L : categoryId;
                        this.title = Objects.requireNonNull(title);
                        this.childCategories = childCategories == null ? new ArrayList<>() : childCategories;
                }
        }

        public record ChildCategoryEditRequest(
                @Min(-1L)
                @Max(9999999999L)
                Long categoryId,

                @NotBlank
                String title,

                @NullOrEmpty(message = "자식 카테고리를 가질 수 없습니다.")
                List<ChildCategoryEditRequest> childCategories
        ) {

                public ChildCategoryEditRequest(Long categoryId, String title, List<ChildCategoryEditRequest> childCategories) {
                        this.categoryId = categoryId == null ? -1L : categoryId;
                        this.title = Objects.requireNonNull(title);
                        this.childCategories = childCategories;
                }
        }
}
