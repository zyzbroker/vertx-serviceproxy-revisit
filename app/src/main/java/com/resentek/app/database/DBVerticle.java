package com.resentek.app.database;

import io.vertx.core.*;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;
import com.resentek.app.common.ChannelDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBVerticle extends AbstractVerticle {

  private static final Logger LOGGER =
    LoggerFactory.getLogger(DBVerticle.class);

  @Override
  public void start(Promise<Void> startPromise){
    LOGGER.info("-- db start --");
    //without service proxy
    //vertx.eventBus().consumer(ChannelDefinition.DB_CHANNEL,this::messageHandler);
    //use service proxy
    bootstrapConsumer(vertx);

    startPromise.complete();
  }

  private void bootstrapConsumer(Vertx vertx) {
    new ServiceBinder(vertx)
      .setAddress(ChannelDefinition.DB_CHANNEL)
      .register(DBService.class, new DBServiceImpl(vertx));
  }

  // private void messageHandler(Message<JsonObject> msg) {    
  //   JsonObject body = (JsonObject) msg.body();
  //   String id = body.getString("id");
  //   LOGGER.info("-- handle get menus request ---");
  //   String action = msg.headers().get("action");
  //   msg.reply(new JsonObject()
  //       .put("word", "hello")
  //       .put("action", action)
  //       .put("id", id));  
  // }
} 
