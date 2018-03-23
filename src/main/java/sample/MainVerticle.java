package sample;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.*;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.handler.sockjs.*;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    Router router = Router.router(vertx);

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
    PermittedOptions permit = new PermittedOptions().setAddress("highway");
    BridgeOptions options = new BridgeOptions()
      .addInboundPermitted(permit)
      .addOutboundPermitted(permit);
    sockJSHandler.bridge(options);

    router.route("/eventbus/*").handler(sockJSHandler);
    router.route().handler(StaticHandler.create().setCachingEnabled(false));

    vertx.setPeriodic(1000, id -> {
      vertx.eventBus().send("highway", "tick");
    });

    vertx.createHttpServer()
      .requestHandler(router::accept)
      .listen(8080);
  }
}
