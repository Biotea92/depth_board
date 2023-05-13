package com.hello.borad.application.presentation.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.repository.CategoryRepository;
import com.hello.borad.dto.request.CategoryCreateRequest;
import com.hello.borad.utils.fixture.CategoryFixtureFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
class BoardControllerDocsTest {

    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    WebApplicationContext webApplicationContext;
    private static final String DOCUMENT_IDENTIFIER = "board/{method-name}/";

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint(), modifyHeaders().remove("Vary")))
                .build();

        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("카테고리는 등록되어야 한다.")
    void createCategory() throws Exception {
        // given
        Category parentCategory = CategoryFixtureFactory.create("카테고리 제목", 1, 1);
        categoryRepository.save(parentCategory);

        CategoryCreateRequest request = new CategoryCreateRequest("카테고리1", parentCategory.getId());
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/board/category")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestFields(
                                fieldWithPath("title").description("카테고리 제목"),
                                fieldWithPath("parentCategoryId").description("부모 카테고리 Id (부모가 없을 시 null or -1)")
                        ),
                        responseFields(
                                fieldWithPath("categoryId").description("category id"),
                                fieldWithPath("title").description("카테고리 제목"),
                                fieldWithPath("depth").description("카테고리 depth"),
                                fieldWithPath("sequence").description("카테고리 순서")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리를 조회한다.")
    void getCategories() throws Exception {
        // given
        Category parentCategory1 = CategoryFixtureFactory.create("카테고리 대분류1", 1, 1);
        Category parentCategory2 = CategoryFixtureFactory.create("카테고리 대분류2", 1, 2);
        Category parentCategory3 = CategoryFixtureFactory.create("카테고리 대분류3", 1, 3);
        categoryRepository.saveAll(List.of(parentCategory1, parentCategory2, parentCategory3));

        Category childCategory1_1 = CategoryFixtureFactory.create("카테고리 1-1", 2, 1, parentCategory1);
        Category childCategory1_2 = CategoryFixtureFactory.create("카테고리 1-2", 2, 2, parentCategory1);
        Category childCategory1_3 = CategoryFixtureFactory.create("카테고리 1-3", 2, 3, parentCategory1);
        categoryRepository.saveAll(List.of(childCategory1_1, childCategory1_2, childCategory1_3));
        parentCategory1.getChildCategories().addAll(List.of(childCategory1_1, childCategory1_2, childCategory1_3));

        Category childCategory2_1 = CategoryFixtureFactory.create("카테고리 2-1", 2, 1, parentCategory2);
        Category childCategory2_2 = CategoryFixtureFactory.create("카테고리 2-2", 2, 2, parentCategory2);
        Category childCategory2_3 = CategoryFixtureFactory.create("카테고리 2-3", 2, 3, parentCategory2);
        categoryRepository.saveAll(List.of(childCategory2_1, childCategory2_2, childCategory2_3));
        parentCategory2.getChildCategories().addAll(List.of(childCategory2_1, childCategory2_2, childCategory2_3));

        Category childCategory3_1 = CategoryFixtureFactory.create("카테고리 3-1", 2, 1, parentCategory3);
        Category childCategory3_2 = CategoryFixtureFactory.create("카테고리 3-2", 2, 2, parentCategory3);
        Category childCategory3_3 = CategoryFixtureFactory.create("카테고리 3-3", 2, 3, parentCategory3);
        categoryRepository.saveAll(List.of(childCategory3_1, childCategory3_2, childCategory3_3));
        parentCategory3.getChildCategories().addAll(List.of(childCategory3_1, childCategory3_2, childCategory3_3));

        // expected
        mockMvc.perform(get("/api/board/category"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        responseFields(
                                fieldWithPath("[].categoryId").description("대분류 category id"),
                                fieldWithPath("[].title").description("대분류 category title"),
                                fieldWithPath("[].depth").description("대분류 깊이"),
                                fieldWithPath("[].sequence").description("대분류 순서"),
                                fieldWithPath("[].hasPost").description("대분류의 게시글 여부"),
                                fieldWithPath("[].childCategoryResponses").description("자식 카테고리"),
                                fieldWithPath("[].childCategoryResponses[].categoryId").description("소분류 category id"),
                                fieldWithPath("[].childCategoryResponses[].title").description("소분류 category title"),
                                fieldWithPath("[].childCategoryResponses[].depth").description("소분류 깊이"),
                                fieldWithPath("[].childCategoryResponses[].sequence").description("소분류 순서"),
                                fieldWithPath("[].childCategoryResponses[].hasPost").description("소분류 게시글 여부")
                        )
                ));
    }
}