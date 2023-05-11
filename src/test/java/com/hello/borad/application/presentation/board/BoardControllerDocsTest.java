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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
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
    }

    @Test
    @DisplayName("카테고리는 등록되어야 한다.")
    void createCategory() throws Exception {
        // given
        Category parentCategory = CategoryFixtureFactory.create("카테고리 제목", 1, 1);
        categoryRepository.save(parentCategory);

        CategoryCreateRequest request = new CategoryCreateRequest("카테고리1", 1L);
        String json = objectMapper.writeValueAsString(request);

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
}