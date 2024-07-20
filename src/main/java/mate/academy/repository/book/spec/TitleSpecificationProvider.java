package mate.academy.repository.book.spec;

import mate.academy.model.Book;
import mate.academy.repository.book.BookSpecificationKeys;
import mate.academy.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    public Specification<Book> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get(BookSpecificationKeys.TITLE), param);
    }

    @Override
    public String getKey() {
        return BookSpecificationKeys.TITLE;
    }
}
