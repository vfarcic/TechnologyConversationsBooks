package com.technologyconversations.books;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.glassfish.jersey.server.JSONP;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("v1/items")
@Produces(MediaType.APPLICATION_JSON)
public class BookApi {

    private static final String ITEMS_URL = "/api/v1/items";

    @GET
    @JSONP(queryParam = "callback")
    public String getAllBooks(@QueryParam("offset") int offset,
                              @QueryParam("count") int count,
                              @QueryParam("callback") String callback) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
        List<Book> books = BookDao.getInstance().getAllBooks(offset, count);
        for (Book book : books) {
            book.setLink(ITEMS_URL + "/" + book.getId());
        }
        return mapper.writeValueAsString(books);
    }

    @DELETE
    @JSONP(queryParam = "callback")
    public void deleteAllBooks() throws Exception {
        BookDao.getInstance().deleteAllBooks();
    }

    @GET
    @Path("/{id}")
    @JSONP(queryParam = "callback")
    public String getBook(@PathParam("id") int id) throws Exception {
        Book book = BookDao.getInstance().getBook(id);
        return new ObjectMapper().writeValueAsString(book);
    }

    @PUT
    @JSONP(queryParam = "callback")
    public void putUser(String bookJson) throws Exception {
        Book book = new ObjectMapper().readValue(bookJson, Book.class);
        BookDao.getInstance().saveOrUpdateBook(book);
    }

    @DELETE
    @Path("/{id}")
    @JSONP(queryParam = "callback")
    public void deleteBook(@PathParam("id") int id) throws Exception {
        BookDao.getInstance().deleteBook(id);
    }

}
