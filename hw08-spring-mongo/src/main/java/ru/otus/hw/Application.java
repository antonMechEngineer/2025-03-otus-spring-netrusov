package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
		BookRepository bookRepository = applicationContext.getBean(BookRepository.class);
		AuthorRepository authorRepository = applicationContext.getBean(AuthorRepository.class);
		GenreRepository genreRepository = applicationContext.getBean(GenreRepository.class);
		CommentRepository commentRepository = applicationContext.getBean(CommentRepository.class);
		Genre genre = new Genre("1", "genre");
		Author author = new Author("1", "author");
		Book book = new Book("1", "bookTitle", author, genre);
		Book updatedBook = new Book("1", "bookTitle" + 4, author, genre);
		Comment comment = new Comment("1", "comment", book);
		genreRepository.save(genre);
		authorRepository.save(author);
		bookRepository.save(book);
		bookRepository.save(updatedBook);
		commentRepository.save(comment);
	}
}
