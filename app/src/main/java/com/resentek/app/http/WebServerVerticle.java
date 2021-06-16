package com.resentek.app.http;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.DeliveryOptions;
import com.resentek.app.database.DBService;
import com.resentek.app.common.ChannelDefinition;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class WebServerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(
        WebServerVerticle.class
      );

  @Override
  public void start(Promise<Void> startPromise) {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.get("/menus").handler(this::getMenus);

    server.requestHandler(router)
      .listen(8080,"localhost", ar->{
        if (ar.succeeded()){
          LOGGER.info("--http server ready --");
          startPromise.complete();
        } else {
          LOGGER.info("-- failed --");
          startPromise.fail(ar.cause());
        }
      });
  }

  private void getMenus(RoutingContext ctx) {
    LOGGER.info("-- get menus --");    
    useServiceProxy(ctx);
    //useEventBusMessaging(ctx);
  }

  private void useServiceProxy(RoutingContext ctx) {
    DBService service = DBService.createProxy(ctx.vertx(), ChannelDefinition.DB_CHANNEL);
    service.getMenus("home")
      .onSuccess(msg ->{
        JsonObject result = (JsonObject) msg;
        HttpServerResponse response = ctx.response();
        response.putHeader("Content-Type", "application/json");
        response.end(result.toString());
      })
      .onFailure(err-> ctx.fail(err));
  }

  private void useEventBusMessaging(RoutingContext ctx) {
    DeliveryOptions opt= new DeliveryOptions()
        .addHeader("action","get-menus");
    vertx.eventBus()
      .request(
        ChannelDefinition.DB_CHANNEL, 
        new JsonObject().put("id","1"),
        opt
    ).onSuccess(msg-> getMenusHandler(msg, ctx))
     .onFailure(err-> ctx.fail(err));
  }

  private void getMenusHandler(Message<Object>reply, RoutingContext ctx){  
    JsonObject body = (JsonObject) reply.body();
    HttpServerResponse response = ctx.response();
    response.putHeader("Content-Type", "application/json");
    response.end(body.toString());
  }
}
