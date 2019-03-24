package repository;

import io.reactivex.Completable;
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

    public Single<Book> getById(String id) {
         final JsonObject query = new JsonObject().put("_id", id);

        return client.rxFindOne(COLLECTION_NAME, query, null)
                .flatMapSingle(result -> {
                    final Book book = new Book(result);

                    return Single.just(book);
                });
    }

    public Single<String> insert(Book book) {
        return client.rxInsert(COLLECTION_NAME, JsonObject.mapFrom(book))
                .flatMapSingle(result -> Single.just(result));

    }

    public Completable update(String id, Book book) {
        final JsonObject query = new JsonObject().put("_id", id);

        return client.rxReplaceDocuments(COLLECTION_NAME, query, JsonObject.mapFrom(book))
                .flatMapCompletable(result -> {
                    if (result.getDocModified() == 1) {
                        return Completable.complete();
                    } else {
                        return Completable.error(new NoSuchElementException("No book with id " + id));
                    }
                });
    }

    public Completable delete(String id) {
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
