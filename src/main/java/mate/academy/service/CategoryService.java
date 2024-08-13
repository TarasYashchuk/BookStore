package mate.academy.service;

import java.util.List;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);
}
