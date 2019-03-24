package service;

import io.reactivex.Completable;
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

    public Single<Book> getById(String id) {
        return bookRepository.getById(id)
                .flatMap(result -> Single.just(result));
    }

    public Single<String> insert(Book book) {
        return bookRepository.insert(book)
                .flatMap(result -> Single.just(result));
    }

    public Completable update(String id, Book book) {
        return bookRepository.update(id, book);
    }

    public Completable delete(String id) {
        return bookRepository.delete(id);
    }

}
