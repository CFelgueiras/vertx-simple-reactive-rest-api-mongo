package handler;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;
import model.Book;
import org.bson.types.ObjectId;
import service.BookService;

public class BookHandler {

    private BookService bookService;

    public BookHandler(BookService bookService) {
        this.bookService = bookService;
    }

    public void getAll(RoutingContext rc) {
        bookService.getAll()
                .subscribe(
                        books -> onSuccessResponse(rc, 200, books),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    public void getOne(RoutingContext rc) {
        final String id = rc.pathParam("id");
        bookService.getById(id)
                .subscribe(
                        book -> onSuccessResponse(rc, 200, book),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    public void insertOne(RoutingContext rc) {
        bookService.insert(rc.getBodyAsJson().mapTo(Book.class))
                .subscribe(
                        book -> onSuccessResponse(rc, 201, book),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    public void updateOne(RoutingContext rc) {
        final String id = rc.pathParam("id");
        bookService.update(id, rc.getBodyAsJson().mapTo(Book.class))
                .subscribe(
                        () -> onSuccessResponse(rc, 204, null),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    public void deleteOne(RoutingContext rc) {
        final String id = rc.pathParam("id");
        bookService.delete(id)
                .subscribe(
                        () -> onSuccessResponse(rc, 204, null),
                        throwable -> onErrorResponse(rc, 400, throwable));
    }

    // Generic responses
    private void onSuccessResponse(RoutingContext rc, int status, Object object) {
        rc.response()
                .setStatusCode(status)
                .putHeader("Content-Type", "application/json")
                .end(Json.encodePrettily(object));
    }

    private void onErrorResponse(RoutingContext rc, int status, Throwable throwable) {
        final JsonObject error = new JsonObject().put("error", new Exception(throwable).getMessage());
        rc.response()
                .setStatusCode(status)
                .putHeader("Content-Type", "application/json")
                .end(Json.encodePrettily(error));
    }

}
