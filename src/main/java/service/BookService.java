package service;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import model.Book;
import repository.BookRepository;

import java.util.List;

public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Single<List<Book>> getAll() {
        return bookRepository.getAll()
                .flatMap(result -> Single.just(result));
    }

    public Maybe<Book> getById(Integer id) {
        return bookRepository.getById(id)
                .flatMap(result -> Maybe.just(result));
    }

    public Maybe<String> insert(Book book) {
        return bookRepository.insert(book)
                .flatMap(result -> Maybe.just(result));
    }

    public Completable update(Integer id, Book book) {
        return bookRepository.update(id, book);
    }

    public Completable delete(Integer id) {
        return bookRepository.delete(id);
    }

}
