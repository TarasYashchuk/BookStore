package mate.academy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.book.UpdateBookRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.book.BookSpecificationBuilder;
import mate.academy.repository.category.CategoryRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookService bookService;

    private final Faker faker = new Faker();
    private CreateBookRequestDto requestDto;
    private Book book;
    private BookDto bookDto;
    private UpdateBookRequestDto updateBookRequestDto;
    private BookSearchParametersDto searchParameters;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(faker.book().author());
        requestDto.setIsbn(faker.code().isbn13());
        requestDto.setTitle(faker.book().title());
        requestDto.setPrice(BigDecimal.TEN);
        requestDto.setDescription(faker.text().text());
        requestDto.setCoverImage(faker.internet().url());
        requestDto.setCategoryIds(List.of(1L, 2L));

        book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());
        book.setCategories(new HashSet<>());

        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoryIds(List.of(1L, 2L));

        updateBookRequestDto = new UpdateBookRequestDto();
        updateBookRequestDto.setAuthor(faker.book().author());
        updateBookRequestDto.setIsbn(faker.code().isbn13());
        updateBookRequestDto.setTitle(faker.book().title());
        updateBookRequestDto.setPrice(BigDecimal.TEN);
        updateBookRequestDto.setDescription(faker.lorem().paragraph());
        updateBookRequestDto.setCoverImage("http://example.com/cover.jpg");
        updateBookRequestDto.setCategoryIds(List.of(1L));

        searchParameters = new BookSearchParametersDto();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Test Save - Valid CreateBookRequestDto returns BookDto")
    void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.save(requestDto);

        assertThat(savedBookDto).isEqualTo(bookDto);
        verify(bookMapper).toModel(requestDto);
        verify(categoryRepository).findAllById(requestDto.getCategoryIds());
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Test Get All - Returns list of BookDtos")
    void getAll_ReturnsListOfBookDtos() {
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtos = bookService.getAll(pageable);

        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);
        verify(bookRepository).findAll(pageable);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Test Get Book By ID - Valid ID returns BookDto")
    void getBookById_ValidId_ReturnsBookDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto resultBook = bookService.getBookById(1L);

        assertThat(resultBook).isEqualTo(bookDto);
        verify(bookRepository).findById(1L);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Test Get Book By ID - Invalid ID throws EntityNotFoundException")
    void getBookById_InvalidId_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book with id 1 not found");

        verify(bookRepository).findById(1L);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Test Delete By ID - Valid ID calls deleteById once")
    void deleteById_ValidId_CallsDeleteByIdOnce() {
        Long bookId = 1L;

        bookService.deleteById(bookId);

        verify(bookRepository).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Test Update Book Details - Valid ID returns updated BookDto")
    void updateBookDetails_ValidId_ReturnsUpdatedBookDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateBookFromDto(updateBookRequestDto, book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto updatedBookDto = bookService.updateBookDetails(1L, updateBookRequestDto);

        assertEquals(bookDto, updatedBookDto);
        verify(bookRepository).findById(1L);
        verify(bookMapper).updateBookFromDto(updateBookRequestDto, book);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Test Update Book Details - Invalid ID throws EntityNotFoundException")
    void updateBookDetails_InvalidId_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                bookService.updateBookDetails(1L, updateBookRequestDto));

        assertEquals("Book not found with id 1", exception.getMessage());
        verify(bookRepository).findById(1L);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Test Search - Valid parameters return list of BookDtos")
    void search_ValidParams_ReturnsListOfBookDtos() {
        Specification<Book> bookSpecification = mock(Specification.class);
        List<Book> books = List.of(book);

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(new PageImpl<>(books));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtos = books.stream()
                .map(b -> {
                    BookDto dto = new BookDto();
                    dto.setId(1L);
                    dto.setAuthor(book.getAuthor());
                    dto.setIsbn(book.getIsbn());
                    dto.setDescription(book.getDescription());
                    dto.setTitle(book.getTitle());
                    dto.setCoverImage(book.getCoverImage());
                    dto.setPrice(book.getPrice());
                    dto.setCategoryIds(List.of(1L, 2L));
                    return dto;
                }).collect(Collectors.toList());

        List<BookDto> result = bookService.search(searchParameters, pageable);

        assertEquals(bookDtos, result);
        verify(bookSpecificationBuilder).build(searchParameters);
        verify(bookRepository).findAll(bookSpecification, pageable);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookSpecificationBuilder, bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Test Search - No results returns empty list")
    void search_NoResults_ReturnsEmptyList() {
        Specification<Book> bookSpecification = mock(Specification.class);
        List<Book> books = List.of();

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(new PageImpl<>(books));

        List<BookDto> result = bookService.search(searchParameters, pageable);

        assertTrue(result.isEmpty());
        verify(bookSpecificationBuilder).build(searchParameters);
        verify(bookRepository).findAll(bookSpecification, pageable);
        verifyNoMoreInteractions(bookSpecificationBuilder, bookRepository, bookMapper);
    }
}
