package com.mycompany.myproject;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.platform.Verticle;

public class ServerTest extends Verticle {
	
	private static final String DOCUMENT_ROOT = "c:/tmp/";
	private static final String PAGE_404 = DOCUMENT_ROOT + "404.html";

	public void start() {
		vertx.createHttpServer()
				.requestHandler(new Handler<HttpServerRequest>() {
					@Override
					public void handle(HttpServerRequest request) {
						switch (request.method()) {
						case "GET":
							serveFile(request.response(), request.uri());
							break;
						default:
							request.response().setStatusCode(500)
									.setStatusMessage("Unsupported Method")
									.end("Unsupported Method");
						}
					}

				}).listen(8080);
	}

	public void serveFile(HttpServerResponse response, String fileName) {
		response.sendFile(DOCUMENT_ROOT + fileName, PAGE_404);
	}

}
