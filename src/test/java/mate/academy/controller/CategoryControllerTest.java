package mate.academy.controller;

import static mate.academy.constans.Constants.ADMIN;
import static mate.academy.constans.Constants.ADMIN_ROLE;
import static mate.academy.constans.Constants.BASE_CATEGORY_URL;
import static mate.academy.constans.Constants.CATEGORY_BOOKS_URL;
import static mate.academy.constans.Constants.CATEGORY_BY_ID_URL;
import static mate.academy.constans.Constants.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.service.CategoryService;
import net.datafaker.Faker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("Test Create Category - Success")
    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    void createCategory_ValidRequest_ReturnsCategory() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName(faker.book().genre());
        expected.setDescription(faker.lorem().paragraph());

        when(categoryService.save(any(CreateCategoryRequestDto.class))).thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(expected);
        MvcResult result = mockMvc.perform(post(BASE_CATEGORY_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Create Category - Forbidden for Non-Admin")
    void createCategory_NonAdminUser_ReturnsForbidden() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName(faker.book().genre());
        expected.setDescription(faker.lorem().paragraph());

        when(categoryService.save(any(CreateCategoryRequestDto.class))).thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(expected);
        mockMvc.perform(post(BASE_CATEGORY_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Get All Categories - Success")
    void getAllCategories_ValidRequest_ReturnsListOfCategories() throws Exception {
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setName(faker.book().genre());
        categoryDto1.setDescription(faker.lorem().paragraph());

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setName(faker.book().genre());
        categoryDto2.setDescription(faker.lorem().paragraph());

        List<CategoryDto> categories = List.of(categoryDto1, categoryDto2);

        when(categoryService.findAll()).thenReturn(categories);

        MvcResult result = mockMvc.perform(get(BASE_CATEGORY_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<CategoryDto> actualCategories = objectMapper.readValue(jsonResponse,
                new TypeReference<>() {});
        assertEquals(categories.size(), actualCategories.size());
        Assertions.assertEquals(categories, actualCategories);

    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Get Category by ID - Success")
    void getCategoryById_ValidRequest_ReturnsCategory() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setName(faker.book().genre());
        expected.setDescription(faker.lorem().paragraph());

        when(categoryService.getById(anyLong())).thenReturn(expected);

        MvcResult result = mockMvc.perform(get(CATEGORY_BY_ID_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CategoryDto actualCategory = objectMapper.readValue(jsonResponse, CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actualCategory);
    }

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Update Category - Success")
    void updateCategory_ValidRequest_ReturnsUpdatedCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(faker.book().genre());
        categoryDto.setDescription(faker.lorem().paragraph());

        when(categoryService.update(eq(categoryId), any(CategoryDto.class)))
                .thenReturn(categoryDto);

        mockMvc.perform(put(CATEGORY_BY_ID_URL, categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andReturn();
        verify(categoryService).update(eq(categoryId), any(CategoryDto.class));
    }

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Update Category - Category Not Found")
    void updateCategory_NonExistentCategory_ReturnsNotFound() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(faker.book().genre());
        categoryDto.setDescription(faker.lorem().paragraph());

        when(categoryService.update(eq(categoryId), any(CategoryDto.class)))
                .thenThrow(new EntityNotFoundException("Category not found"));

        mockMvc.perform(put(CATEGORY_BY_ID_URL, categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isNotFound());

        verify(categoryService).update(eq(categoryId), any(CategoryDto.class));
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Update Category - Forbidden for Non-Admin")
    void updateCategory_NonAdminUser_ReturnsForbidden() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(faker.book().genre());
        categoryDto.setDescription(faker.lorem().paragraph());

        mockMvc.perform(put(CATEGORY_BY_ID_URL, categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Delete Category - Success")
    void deleteCategory_ValidRequest_ReturnsNoContent() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete(CATEGORY_BY_ID_URL, categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteById(categoryId);
    }

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Delete Category - Category Not Found")
    void deleteCategory_NonExistentCategory_ReturnsNotFound() throws Exception {
        Long categoryId = 1L;

        doThrow(new EntityNotFoundException("Category not found"))
                .when(categoryService).deleteById(categoryId);

        mockMvc.perform(delete(CATEGORY_BY_ID_URL, categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(categoryService).deleteById(categoryId);
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Delete Category - Forbidden for Non-Admin")
    void deleteCategory_NonAdminUser_ReturnsForbidden() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete(CATEGORY_BY_ID_URL, categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Get Books by Category ID - Success")
    void getBooksByCategoryId_ValidRequest_ReturnsListOfBooks() throws Exception {
        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds();
        book1.setId(1L);
        book1.setPrice(BigDecimal.valueOf(10.99));
        book1.setIsbn(faker.code().isbn13());
        book1.setTitle(faker.book().title());
        book1.setAuthor(faker.book().author());
        book1.setCoverImage("http://example.com/cover.jpg");
        book1.setDescription(faker.lorem().paragraph());

        BookDtoWithoutCategoryIds book2 = new BookDtoWithoutCategoryIds();
        book2.setId(2L);
        book2.setPrice(BigDecimal.valueOf(15.99));
        book2.setIsbn(faker.code().isbn13());
        book2.setTitle(faker.book().title());
        book2.setAuthor(faker.book().author());
        book2.setCoverImage("http://example2.com/cover.jpg");
        book2.setDescription(faker.lorem().paragraph());

        Long categoryId = 1L;
        List<BookDtoWithoutCategoryIds> books = List.of(book1, book2);

        when(categoryService.getBooksByCategoryId(eq(categoryId), any(Pageable.class)))
                .thenReturn(books);

        MvcResult result = mockMvc.perform(get(CATEGORY_BOOKS_URL, categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDtoWithoutCategoryIds> actualBooks = objectMapper.readValue(jsonResponse,
                new TypeReference<>() {});

        assertEquals(2, actualBooks.size());
        assertTrue(actualBooks.containsAll(books));
        verify(categoryService).getBooksByCategoryId(eq(categoryId), any(Pageable.class));
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Get Books By Category ID - Category Not Found")
    void getBooksByCategoryId_NonExistentCategory_ReturnsNotFound() throws Exception {
        Long categoryId = 1L;

        when(categoryService.getBooksByCategoryId(eq(categoryId), any(Pageable.class)))
                .thenThrow(new EntityNotFoundException("Category not found"));

        mockMvc.perform(get(CATEGORY_BOOKS_URL, categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound());

        verify(categoryService).getBooksByCategoryId(eq(categoryId), any(Pageable.class));
    }

    @Test
    @DisplayName("Test Get Books By Category ID - Forbidden for Non-User")
    void getBooksByCategoryId_NonUser_ReturnsForbidden() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(get(CATEGORY_BOOKS_URL, categoryId)
                        .with(user("guest").roles("GUEST")))
                .andExpect(status().isForbidden());
    }
}
