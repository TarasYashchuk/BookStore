package mate.academy;

import java.math.BigDecimal;
import mate.academy.model.Book;
import mate.academy.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    private BookRepository bookRepository;

    @Autowired
    public BookStoreApplication(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setAuthor("Jack");
            book.setId(1L);
            book.setPrice(BigDecimal.valueOf(13));
            book.setDescription("123");
            book.setCoverImage("/images");
            book.setIsbn("123");
            book.setTitle("title");

            bookRepository.save(book);
        };
    }
}
