package com.technologyconversations.books;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Server {

    public static final String BASE_API_URI = "http://localhost:8080/api/";

    public boolean getFileCacheEnabled() {
        return false;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        final HttpServer httpServer = server.startServer();
        System.out.println("Press enter to stop the server...");
        System.in.read();
        httpServer.shutdown();
    }

    public HttpServer startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("com.technologyconversations.books");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_API_URI), rc);
        server.getServerConfiguration().addHttpHandler(getHttpHandler(), "/page");
        return server;
    }

    public HttpHandler getHttpHandler() {
        StaticHttpHandler handler = new StaticHttpHandler("src/main/resources/webapp/");
        handler.setFileCacheEnabled(getFileCacheEnabled());
        return handler;
    }

}
