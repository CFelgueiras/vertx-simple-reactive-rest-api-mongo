import io.vertx.reactivex.core.Vertx;
import verticle.MainVerticle;

public class Main {
    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();

        vertx.rxDeployVerticle(MainVerticle.class.getName())
                .subscribe(
                        result -> System.out.println("New verticle started!"),
                        error -> System.out.println("Error occurred before deploying a new verticle: " + error.getMessage()));
    }

}
