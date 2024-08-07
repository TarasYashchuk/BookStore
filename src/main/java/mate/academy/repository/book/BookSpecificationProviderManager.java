package mate.academy.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.model.Book;
import mate.academy.repository.specification.SpecificationProvider;
import mate.academy.repository.specification.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Cant get specification with key: " + key));
    }
}
