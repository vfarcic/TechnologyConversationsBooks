package com.technologyconversations.books;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;

public class StandaloneServer extends Server {

    @Override
    public boolean getFileCacheEnabled() {
        return true;
    }

    @Override
    public HttpHandler getHttpHandler() {
        return new CLStaticHttpHandler(StandaloneServer.class.getClassLoader(), "webapp/");
    }


}
