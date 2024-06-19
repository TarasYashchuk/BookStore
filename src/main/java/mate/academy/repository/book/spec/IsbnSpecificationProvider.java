package mate.academy.repository.book.spec;

import mate.academy.model.Book;
import mate.academy.repository.SpecificationProvider;
import mate.academy.repository.book.BookSpecificationKeys;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {

    public Specification<Book> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(BookSpecificationKeys.ISBN), param);
    }

    @Override
    public String getKey() {
        return BookSpecificationKeys.ISBN;
    }
}

