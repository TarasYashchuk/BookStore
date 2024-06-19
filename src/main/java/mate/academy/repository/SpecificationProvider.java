package mate.academy.repository;

import mate.academy.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    Specification<Book> getSpecification(String param);

    String getKey();
}
