package mate.academy.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookSearchParametersDto;
import mate.academy.model.Book;
import mate.academy.repository.specification.SpecificationBuilder;
import mate.academy.repository.specification.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> specification = Specification.where(null);

        specification = addSpecification(specification,
                BookSpecificationKeys.TITLE, searchParameters.getTitle());
        specification = addSpecification(specification,
                BookSpecificationKeys.PRICE, searchParameters.getPrice());
        specification = addSpecification(specification,
                BookSpecificationKeys.ISBN, searchParameters.getIsbn());
        specification = addSpecification(specification,
                BookSpecificationKeys.AUTHOR, searchParameters.getAuthor());

        return specification;
    }

    private Specification<Book> addSpecification(Specification<Book> specification,
                                                 String key, String value) {
        if (value != null && !value.isEmpty()) {
            specification = specification
                    .and(bookSpecificationProviderManager.getSpecificationProvider(key)
                            .getSpecification(value));
        }
        return specification;
    }
}
