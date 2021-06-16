package com.resentek.app.database;

import io.vertx.core.Vertx;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class DBServiceImpl implements DBService {
	private Vertx vertx;

	public DBServiceImpl(Vertx vertx) {
		this.vertx = vertx;
	}

	public Future<JsonObject> getMenus(String page) {
		return Future.succeededFuture(
			new JsonObject()
				.put("page", page)
				.put("word","Hello")
				.put("action", "getMenus"));
	}

}