package repository;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.mongo.MongoClient;
import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BookRepository {

    private static final String COLLECTION_NAME = "books";

    private final MongoClient client;

    public BookRepository(MongoClient client) {
        this.client = client;
    }

    public Single<List<Book>> getAll() {
        final JsonObject query = new JsonObject();

        return client.rxFind(COLLECTION_NAME, query)
                .flatMap(result -> {
                    final List<Book> books = new ArrayList<>();
                    result.forEach(book -> books.add(new Book(book)));

                    return Single.just(books);
                });
    }

    public Maybe<Book> getById(Integer id) {
        final JsonObject query = new JsonObject().put("_id", id);
        final JsonObject fields = JsonObject.mapFrom(new Book());

        return client.rxFindOne(COLLECTION_NAME, query, fields)
                .flatMap(result -> {
                    if (result.size() == 0) {
                        throw new NoSuchElementException("No book with id " + id);
                    }

                    return Maybe.just(result.mapTo(Book.class));
                });
    }

    public Maybe<String> insert(Book book) {
        return client.rxInsert(COLLECTION_NAME, JsonObject.mapFrom(book))
                .flatMap(result -> Maybe.just(result));

    }

    public Completable update(Integer id, Book book) {
        final JsonObject query = new JsonObject().put("_id", id);

        return client.rxUpdateCollection(COLLECTION_NAME, query, JsonObject.mapFrom(book))
                .flatMapCompletable(result -> {
                    if (result.getDocModified() == 1) {
                        return Completable.complete();
                    } else {
                        return Completable.error(new NoSuchElementException("No book with id " + id));
                    }
                });    }

    public Completable delete(Integer id) {
        final JsonObject query = new JsonObject().put("_id", id);

        return client.rxRemoveDocument(COLLECTION_NAME, query)
                .flatMapCompletable(result -> {
                    if (result.getRemovedCount() == 1) {
                        return Completable.complete();
                    } else {
                        return Completable.error(new NoSuchElementException("No book with id " + id));
                    }
                });
    }

}
