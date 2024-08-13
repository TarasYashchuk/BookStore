package mate.academy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Book;
import mate.academy.model.Category;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.category.CategoryRepository;
import mate.academy.service.impl.CategoryServiceImpl;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    private final Faker faker = new Faker();

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save category - Valid CategoryDto returns saved CategoryDto")
    void saveCategory_ValidCategoryDto_ReturnsSavedCategoryDto() {
        CreateCategoryRequestDto categoryDto = new CreateCategoryRequestDto();
        categoryDto.setName(faker.book().genre());
        categoryDto.setDescription(faker.lorem().paragraph());

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName(category.getName());
        savedCategory.setDescription(category.getDescription());

        CategoryDto savedCategoryDto = new CategoryDto();
        savedCategoryDto.setName(savedCategory.getName());
        savedCategoryDto.setDescription(savedCategory.getDescription());

        when(categoryMapper.toModel(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(savedCategoryDto);

        CategoryDto result = categoryService.save(categoryDto);

        assertEquals(savedCategoryDto, result);
        verify(categoryMapper).toModel(categoryDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(savedCategory);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Find all categories - Returns list of CategoryDtos")
    void findAllCategories_ReturnsListOfCategoryDtos() {
        Category category = new Category();
        category.setId(1L);
        category.setName(faker.book().genre());
        category.setDescription(faker.text().text());

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName(faker.book().genre());
        category2.setDescription(faker.text().text());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDescription(category.getDescription());
        categoryDto.setName(category.getName());

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setDescription(category2.getDescription());
        categoryDto2.setName(category2.getName());

        List<Category> categories = List.of(category, category2);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        List<CategoryDto> result = categoryService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(categoryDto, categoryDto2);

        verify(categoryRepository).findAll();
        verify(categoryMapper).toDto(category);
        verify(categoryMapper).toDto(category2);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Get category by ID - Valid ID returns CategoryDto")
    void getCategoryById_ValidId_ReturnsCategoryDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName(faker.book().genre());
        category.setDescription(faker.text().text());
        category.setBooks(new HashSet<>());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto resultDto = categoryService.getById(1L);

        assertThat(resultDto).isEqualTo(categoryDto);
        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Get category by ID - Invalid ID throws EntityNotFoundException")
    void getCategoryById_InvalidId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category with id " + 1L + " not found");

        verify(categoryRepository).findById(1L);
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("Update category - Valid ID and CategoryDto returns updated CategoryDto")
    void updateCategory_ValidIdAndCategoryDto_ReturnsUpdatedCategoryDto() {

        Category existingCategory = new Category();
        existingCategory.setName(faker.book().genre());
        existingCategory.setDescription(faker.lorem().paragraph());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(faker.book().genre());
        categoryDto.setDescription(faker.lorem().paragraph());

        Category updatedCategory = new Category();
        updatedCategory.setName(categoryDto.getName());
        updatedCategory.setDescription(categoryDto.getDescription());

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setName(updatedCategory.getName());
        expectedCategoryDto.setDescription(updatedCategory.getDescription());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedCategoryDto);

        CategoryDto resultCategoryDto = categoryService.update(1L, categoryDto);

        assertEquals(expectedCategoryDto, resultCategoryDto);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(existingCategory);
        verify(categoryMapper).toDto(updatedCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Update category - Invalid ID throws EntityNotFoundException")
    void updateCategory_InvalidId_ThrowsEntityNotFoundException() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(faker.book().genre());
        categoryDto.setDescription(faker.lorem().paragraph());

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(1L, categoryDto)
        );

        assertThat(exception.getMessage()).isEqualTo("Category not found");
        verify(categoryRepository).findById(1L);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Delete category by ID - Valid ID marks category as deleted")
    void deleteById_ValidId_MarksCategoryAsDeleted() {
        Category existingCategory = new Category();
        existingCategory.setName(faker.book().genre());
        existingCategory.setDescription(faker.text().text());
        existingCategory.setDeleted(false);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        categoryService.deleteById(1L);

        assertTrue(existingCategory.isDeleted());
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(existingCategory);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Get books by category ID - Valid ID returns list of BookDtosWithoutCategoryIds")
    void getBooksByCategoryId_ValidId_ReturnsListOfBookDtosWithoutCategoryIds() {
        Book book = new Book();
        book.setTitle(faker.book().title());
        book.setAuthor(faker.book().author());
        book.setIsbn(faker.code().isbn13());
        book.setPrice(BigDecimal.TEN);
        book.setDescription(faker.text().text());
        book.setCoverImage(faker.internet().url());

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setTitle(book.getTitle());
        bookDtoWithoutCategoryIds.setAuthor(book.getAuthor());
        bookDtoWithoutCategoryIds.setIsbn(book.getIsbn());
        bookDtoWithoutCategoryIds.setPrice(book.getPrice());
        bookDtoWithoutCategoryIds.setDescription(book.getDescription());
        bookDtoWithoutCategoryIds.setCoverImage(book.getCoverImage());

        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findAllByCategories_Id(1L, pageable)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(bookDtoWithoutCategoryIds);

        List<BookDtoWithoutCategoryIds> result = categoryService.getBooksByCategoryId(1L, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(bookDtoWithoutCategoryIds);
        verify(bookRepository).findAllByCategories_Id(1L, pageable);
        verify(bookMapper).toDtoWithoutCategories(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
