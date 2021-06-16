package com.resentek.app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.DeploymentOptions;
import com.resentek.app.database.DBVerticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootstrapVerticle extends AbstractVerticle {
  private static final Logger LOGGER =
    LoggerFactory.getLogger(BootstrapVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOGGER.info("-- bootstrap --");
    Promise<String> dbDeploy = Promise.promise();
    vertx.deployVerticle(new DBVerticle(), dbDeploy); 
    
    dbDeploy.future().compose(id->{
      Promise<String> httpDeploy = Promise.promise();
      vertx.deployVerticle("com.resentek.app.http.WebServerVerticle",
        new DeploymentOptions().setInstances(2), httpDeploy);
      return httpDeploy.future();
    }).onComplete(ar ->{
      if (ar.succeeded()){
        startPromise.complete();
      } else {
        startPromise.fail(ar.cause());
      }
    });
  }
}
