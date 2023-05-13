package com.hello.borad.application.presentation.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.borad.domain.board.entity.Category;
import com.hello.borad.domain.board.entity.Post;
import com.hello.borad.domain.board.repository.CategoryRepository;
import com.hello.borad.domain.board.repository.PostRepository;
import com.hello.borad.dto.request.CategoryCreateRequest;
import com.hello.borad.dto.request.CategoryEditRequest;
import com.hello.borad.dto.request.CategoryEditRequest.ChildCategoryEditRequest;
import com.hello.borad.dto.request.CategoryEditRequest.ParentCategoryEditRequest;
import com.hello.borad.dto.request.PostCreateRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
    PostRepository postRepository;
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
                .apply(documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("https")
                        .withHost("accura.com")
                        .withPort(443))
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

    @Test
    @DisplayName("카테고리 전체를 수정한다.")
    void editCategories() throws Exception {
        // given
        Category parentCategory1 = CategoryFixtureFactory.create("test_title", 1, 1);
        Category parentCategory2 = CategoryFixtureFactory.create("test_title", 1, 2);
        Category parentCategory3 = CategoryFixtureFactory.create("test_title", 1, 3);
        categoryRepository.saveAll(List.of(parentCategory1, parentCategory2, parentCategory3));

        Category childCategory1_1 = CategoryFixtureFactory.create("test_title", 2, 1, parentCategory1);
        Category childCategory1_2 = CategoryFixtureFactory.create("test_title", 2, 2, parentCategory1);
        Category childCategory1_3 = CategoryFixtureFactory.create("test_title", 2, 3, parentCategory1);
        categoryRepository.saveAll(List.of(childCategory1_1, childCategory1_2, childCategory1_3));

        Category childCategory2_1 = CategoryFixtureFactory.create("test_title", 2, 1, parentCategory2);
        Category childCategory2_2 = CategoryFixtureFactory.create("test_title", 2, 2, parentCategory2);
        Category childCategory2_3 = CategoryFixtureFactory.create("test_title", 2, 3, parentCategory2);
        categoryRepository.saveAll(List.of(childCategory2_1, childCategory2_2, childCategory2_3));

        CategoryEditRequest request = new CategoryEditRequest(
                List.of(new ParentCategoryEditRequest(
                                parentCategory2.getId(),
                                "카테고리 대분류1",
                                List.of(new ChildCategoryEditRequest(childCategory1_3.getId(), "카테고리 소분류 1-1", List.of()),
                                        new ChildCategoryEditRequest(childCategory1_2.getId(), "카테고리 소분류 1-2", List.of()),
                                        new ChildCategoryEditRequest(parentCategory3.getId(), "카테고리 소분류 1-3", List.of())
                                )
                        ),
                        new ParentCategoryEditRequest(
                                childCategory2_1.getId(),
                                "카테고리 대분류2",
                                List.of(new ChildCategoryEditRequest(-1L, "카테고리 소분류 2-1", List.of()))
                        ),
                        new ParentCategoryEditRequest(
                                -1L,
                                "카테고리 대분류3",
                                List.of()
                        )
                ),
                List.of(parentCategory1.getId(), childCategory1_1.getId(), childCategory2_2.getId(), childCategory2_3.getId())
        );
        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);

        // expected
        mockMvc.perform(put("/api/board/category")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        requestFields(
                                fieldWithPath("parentCategories").description("부모 category"),
                                fieldWithPath("parentCategories[].categoryId").description("부모 category id 새로운 카테고리면 -1을 입력해주세요."),
                                fieldWithPath("parentCategories[].title").description("부모 category title"),
                                fieldWithPath("parentCategories[].childCategories").description("자식 category"),
                                fieldWithPath("parentCategories[].childCategories[].categoryId").description("자식 category id 새로운 카테고리면 -1을 입력해주세요."),
                                fieldWithPath("parentCategories[].childCategories[].title").description("자식 category title"),
                                fieldWithPath("parentCategories[].childCategories[].childCategories").description("빈 리스트[] 거나 null 이어야 합니다."),
                                fieldWithPath("removedCategoryIds").description("삭제 할 카테고리 Ids")

                        ),
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

    @Test
    @DisplayName("글은 등록되어야 한다.")
    void createPost() throws Exception {
        // given
        Category parentCategory = CategoryFixtureFactory.create("카테고리 제목", 1, 1);
        Category childCategory = CategoryFixtureFactory.create("test_title", 2, 1, parentCategory);
        categoryRepository.save(parentCategory);
        categoryRepository.save(childCategory);

        PostCreateRequest request = new PostCreateRequest("제목", "내용");
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/board/category/{categoryId}/post", childCategory.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("categoryId").description("글 등록할 카테고리 id")
                        ),
                        requestFields(
                                fieldWithPath("title").description("글 제목"),
                                fieldWithPath("content").description("글 내용")
                        ),
                        responseFields(
                                fieldWithPath("postId").description("post id"),
                                fieldWithPath("title").description("post 제목"),
                                fieldWithPath("content").description("post 내용"),
                                fieldWithPath("createdAt").description("post 등록일"),
                                fieldWithPath("categoryResponse").description("글의 카테고리"),
                                fieldWithPath("categoryResponse.categoryId").description("category id"),
                                fieldWithPath("categoryResponse.title").description("카테고리 제목"),
                                fieldWithPath("categoryResponse.depth").description("카테고리 depth"),
                                fieldWithPath("categoryResponse.sequence").description("카테고리 순서")
                        )
                ));
    }

    @Test
    @DisplayName("글은 조회되어야 한다.")
    void getPost() throws Exception {
        // given
        Category parentCategory = CategoryFixtureFactory.create("카테고리 제목", 1, 1);
        Category childCategory = CategoryFixtureFactory.create("test_title", 2, 1, parentCategory);
        categoryRepository.save(parentCategory);
        categoryRepository.save(childCategory);

        List<Post> posts = new ArrayList<>(LongStream.range(1L, 6L)
                .mapToObj(i -> Post.create("제목" + i, "내용" + i, childCategory))
                .toList());
        postRepository.saveAll(posts);

        // expected
        mockMvc.perform(get("/api/board/category/{categoryId}/posts", childCategory.getId()))
                .andExpect(status().isOk())
                .andDo(document(DOCUMENT_IDENTIFIER,
                        pathParameters(
                                parameterWithName("categoryId").description("글 조회 할 카테고리 id")
                        ),
                        responseFields(
                                fieldWithPath("[].postId").description("post id"),
                                fieldWithPath("[].title").description("post 제목"),
                                fieldWithPath("[].content").description("post 내용"),
                                fieldWithPath("[].createdAt").description("post 등록일"),
                                fieldWithPath("[].categoryResponse").description("글의 카테고리"),
                                fieldWithPath("[].categoryResponse.categoryId").description("category id"),
                                fieldWithPath("[].categoryResponse.title").description("카테고리 제목"),
                                fieldWithPath("[].categoryResponse.depth").description("카테고리 depth"),
                                fieldWithPath("[].categoryResponse.sequence").description("카테고리 순서")
                        )
                ));
    }
}