package mate.academy.controller;

import static mate.academy.constans.Constants.ADMIN;
import static mate.academy.constans.Constants.ADMIN_ROLE;
import static mate.academy.constans.Constants.BASE_BOOK_URL;
import static mate.academy.constans.Constants.BOOK_BY_ID_URL;
import static mate.academy.constans.Constants.BOOK_SEARCH_URL;
import static mate.academy.constans.Constants.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
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
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.book.UpdateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.service.BookService;
import net.datafaker.Faker;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookControllerTest {

    protected static MockMvc mockMvc;

    @MockBean
    private BookService bookService;

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

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Create Book - Valid CreateBookRequestDto returns valid BookDto")
    void createBook_ValidCreateBookRequestDto_ReturnsValidBookDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(faker.book().author());
        requestDto.setIsbn(faker.code().isbn10());
        requestDto.setTitle(faker.book().title());
        requestDto.setPrice(BigDecimal.valueOf(10.00));
        requestDto.setDescription(faker.lorem().paragraph());
        requestDto.setCoverImage("http://example.com/cover.jpg");
        requestDto.setCategoryIds(List.of(1L, 2L));

        BookDto expectedResponse = new BookDto();
        expectedResponse.setId(1L);
        expectedResponse.setTitle(requestDto.getTitle());
        expectedResponse.setAuthor(requestDto.getAuthor());
        expectedResponse.setIsbn(requestDto.getIsbn());
        expectedResponse.setPrice(requestDto.getPrice());
        expectedResponse.setDescription(requestDto.getDescription());
        expectedResponse.setCoverImage(requestDto.getCoverImage());
        expectedResponse.setCategoryIds(requestDto.getCategoryIds());

        when(bookService.save(any(CreateBookRequestDto.class)))
                .thenReturn(expectedResponse);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post(BASE_BOOK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expectedResponse, actual, "id");
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Get All Books - Valid request returns list of books")
    void getAllBooks_ValidRequest_ReturnsListOfBooks() throws Exception {
        BookDto bookDto1 = new BookDto();
        bookDto1.setId(1L);
        bookDto1.setAuthor(faker.book().author());
        bookDto1.setTitle(faker.book().title());
        bookDto1.setDescription(faker.text().text());
        bookDto1.setIsbn(faker.code().isbn13());
        bookDto1.setPrice(BigDecimal.valueOf(19.22));
        bookDto1.setCategoryIds(List.of(1L));
        bookDto1.setCoverImage(faker.internet().url());

        BookDto bookDto2 = new BookDto();
        bookDto2.setId(2L);
        bookDto2.setAuthor(faker.book().author());
        bookDto2.setTitle(faker.book().title());
        bookDto2.setDescription(faker.text().text());
        bookDto2.setIsbn(faker.code().isbn13());
        bookDto2.setPrice(BigDecimal.valueOf(20.22));
        bookDto2.setCategoryIds(List.of(2L));
        bookDto2.setCoverImage(faker.internet().url());

        List<BookDto> books = List.of(bookDto1, bookDto2);
        Pageable pageable = PageRequest.of(0, 10);

        when(bookService.getAll(pageable)).thenReturn(books);

        MvcResult result = mockMvc.perform(get(BASE_BOOK_URL)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDto> actualBooks = objectMapper.readValue(jsonResponse,
                new TypeReference<>() {
                });
        assertEquals(books.size(), actualBooks.size());
        Assertions.assertEquals(books, actualBooks);

    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Get Book By ID - Valid ID returns BookDto")
    void getBookById_ValidId_ReturnsBookDto() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setAuthor(faker.book().author());
        bookDto.setTitle(faker.book().title());
        bookDto.setDescription(faker.text().text());
        bookDto.setIsbn(faker.code().isbn13());
        bookDto.setPrice(BigDecimal.valueOf(19.22));
        bookDto.setCategoryIds(List.of(1L));
        bookDto.setCoverImage(faker.internet().url());

        when(bookService.getBookById(anyLong())).thenReturn(bookDto);

        MvcResult result = mockMvc.perform(get(BOOK_BY_ID_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        EqualsBuilder.reflectionEquals(bookDto, actualBook);
    }

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Delete Book By ID - Valid ID returns No Content")
    void deleteBookById_asAdmin_ValidId_ReturnsNoContent() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setAuthor(faker.book().author());
        bookDto.setTitle(faker.book().title());
        bookDto.setDescription(faker.text().text());
        bookDto.setIsbn(faker.code().isbn13());
        bookDto.setPrice(BigDecimal.valueOf(19.22));
        bookDto.setCategoryIds(List.of(1L));
        bookDto.setCoverImage(faker.internet().url());

        mockMvc.perform(delete(BOOK_BY_ID_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Delete Book By ID - Book not found returns Not Found")
    void deleteBookById_BookNotFound_ReturnsNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Book not found"))
                .when(bookService).deleteById(anyLong());

        mockMvc.perform(delete(BOOK_BY_ID_URL, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = ADMIN, roles = {"ADMIN"})
    @Test
    @DisplayName("Test Update Book - Valid UpdateBookRequestDto returns updated BookDto")
    void updateBook_asAdmin_ValidUpdateRequest_ReturnsUpdatedBookDto() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(faker.book().title());
        bookDto.setAuthor(faker.book().author());
        bookDto.setIsbn(faker.code().isbn13());
        bookDto.setPrice(BigDecimal.TEN);
        bookDto.setDescription(faker.lorem().paragraph());
        bookDto.setCoverImage(faker.internet().url());
        bookDto.setCategoryIds(List.of(1L, 2L));

        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto();
        updateBookRequestDto.setTitle(faker.book().title());
        updateBookRequestDto.setAuthor(faker.book().author());
        updateBookRequestDto.setIsbn(faker.code().isbn13());
        updateBookRequestDto.setPrice(BigDecimal.TEN);
        updateBookRequestDto.setDescription(faker.lorem().paragraph());
        updateBookRequestDto.setCoverImage("http://example.com/cover.jpg");
        updateBookRequestDto.setCategoryIds(List.of(1L, 2L));

        when(bookService.updateBookDetails(anyLong(), any(UpdateBookRequestDto.class)))
                .thenReturn(bookDto);

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        MvcResult result = mockMvc.perform(put(BOOK_BY_ID_URL, 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actualBook = objectMapper.readValue(jsonResponse, BookDto.class);

        assertNotNull(actualBook);
        EqualsBuilder.reflectionEquals(bookDto, actualBook);
    }

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Update Book - Book not found returns Not Found")
    void updateBook_BookNotFound_ReturnsNotFound() throws Exception {

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(faker.book().title());
        bookDto.setAuthor(faker.book().author());
        bookDto.setIsbn(faker.code().isbn13());
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setDescription(faker.text().text());
        bookDto.setCoverImage("http://example.com/cover.jpg");
        bookDto.setCategoryIds(List.of(1L, 2L));

        UpdateBookRequestDto updateBookRequestDto = new UpdateBookRequestDto();
        updateBookRequestDto.setAuthor(faker.book().author());
        updateBookRequestDto.setIsbn(faker.code().isbn13());
        updateBookRequestDto.setTitle(faker.book().title());
        updateBookRequestDto.setPrice(BigDecimal.TEN);
        updateBookRequestDto.setDescription(faker.text().text());
        updateBookRequestDto.setCoverImage("http://newexample.com/cover.jpg");
        updateBookRequestDto.setCategoryIds(List.of(1L));
        when(bookService.updateBookDetails(anyLong(), any(UpdateBookRequestDto.class)))
                .thenThrow(new EntityNotFoundException("Book not found"));

        String jsonRequest = objectMapper.writeValueAsString(updateBookRequestDto);

        mockMvc.perform(put(BOOK_BY_ID_URL, 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = USER)
    @Test
    @DisplayName("Test Search Books As User - Valid search parameters return list of books")
    void searchBooks_asUser_ValidSearchParameters_ReturnsListOfBooks() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(faker.book().title());
        bookDto.setAuthor(faker.book().author());
        bookDto.setIsbn(faker.code().isbn13());
        bookDto.setPrice(BigDecimal.TEN);
        bookDto.setDescription(faker.lorem().paragraph());
        bookDto.setCoverImage(faker.internet().url());
        bookDto.setCategoryIds(List.of(1L, 2L));

        List<BookDto> books = List.of(bookDto);
        when(bookService.search(any(BookSearchParametersDto.class), any(Pageable.class)))
                .thenReturn(books);

        MvcResult result = mockMvc.perform(get(BOOK_SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDto> actualBooks = objectMapper.readValue(jsonResponse,
                new TypeReference<>() {
                });

        assertNotNull(actualBooks);
        assertEquals(books.size(), actualBooks.size());
        assertEquals(books.get(0).getId(), actualBooks.get(0).getId());
    }

    @WithMockUser(username = ADMIN, roles = {ADMIN_ROLE})
    @Test
    @DisplayName("Test Search Books As Admin - Valid search parameters return list of books")
    void searchBooks_asAdmin_ValidSearchParameters_ReturnsListOfBooks() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(faker.book().title());
        bookDto.setAuthor(faker.book().author());
        bookDto.setIsbn(faker.code().isbn13());
        bookDto.setPrice(BigDecimal.TEN);
        bookDto.setDescription(faker.lorem().paragraph());
        bookDto.setCoverImage(faker.internet().url());
        bookDto.setCategoryIds(List.of(1L, 2L));

        List<BookDto> books = List.of(bookDto);
        when(bookService.search(any(BookSearchParametersDto.class), any(Pageable.class)))
                .thenReturn(books);

        MvcResult result = mockMvc.perform(get(BOOK_SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDto> actualBooks = objectMapper.readValue(jsonResponse,
                new TypeReference<>() {
                });

        assertNotNull(actualBooks);
        assertEquals(books.size(), actualBooks.size());
        assertEquals(books.get(0).getId(), actualBooks.get(0).getId());
    }
}
