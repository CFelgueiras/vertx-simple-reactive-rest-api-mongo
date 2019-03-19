package verticle;

import handler.BookHandler;
import io.reactivex.Single;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.Router;
import repository.BookRepository;
import router.BookRouter;
import service.BookService;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        final ConfigStoreOptions store = new ConfigStoreOptions().setType("env");
        final ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(store);
        final ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

        retriever.rxGetConfig()
                .flatMap(configurations -> {
                    final MongoClient client = createMongoClient(vertx, configurations);

                    final BookRepository bookRepository = new BookRepository(client);
                    final BookService bookService = new BookService(bookRepository);
                    final BookHandler bookHandler = new BookHandler(bookService);
                    final BookRouter bookRouter = new BookRouter(vertx, bookHandler);

                    return createHttpServer(bookRouter.getRouter(), configurations);
                })
                .subscribe(server -> System.out.println("HTTP Server listening on port " + server.actualPort()));
    }

    // Private methods
    private MongoClient createMongoClient(Vertx vertx, JsonObject config) {
        final JsonObject configurations = new JsonObject()
                .put("host", config.getString("HOST"))
                .put("username", config.getString("USERNAME"))
                .put("password", config.getString("PASSWORD"))
                .put("authSource", config.getString("AUTHSOURCE"))
                .put("db_name", config.getString("DB_NAME"));

        return MongoClient.createShared(vertx, configurations);
    }

    private Single<HttpServer> createHttpServer(Router router, JsonObject config) {
        return vertx
                .createHttpServer()
                .requestHandler(router)
                .rxListen(config.getInteger("HTTP_PORT"));
    }

}
