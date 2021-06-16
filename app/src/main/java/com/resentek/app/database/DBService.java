package com.resentek.app.database;

import io.vertx.core.Vertx;
import io.vertx.codegen.annotations.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

@ProxyGen
@VertxGen
public interface DBService {
    static DBService create(Vertx vertx){
        return new DBServiceImpl(vertx);
    }

    @GenIgnore
    static DBService createProxy(Vertx vertx, String address){
        return new DBServiceVertxEBProxy(vertx, address);
    }

    Future<JsonObject> getMenus(String page);
}
