package com.technologyconversations.books;

import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.*;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookApiTest extends CommonTest {

    private static HttpServer server;
    private WebTarget itemsTarget;
    private ObjectMapper objectMapper;

    @BeforeClass
    public static void beforeBookApiTestClass() {
        server = new Server().startServer();
    }

    @Before
    public void beforeBookApiTest() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/api/");
        itemsTarget = target.path("v1/items");
        objectMapper = new ObjectMapper();
    }

    @After
    public void afterUserResourceTest() throws Exception {
        bookDao.deleteAllBooks();
    }

    @AfterClass
    public static void afterUserResourceTestClass() {
        server.shutdown();
    }

    @Test
    public void v1ItemsShouldReturnStatus200() {
        assertThat(itemsTarget.request().head().getStatus(), is(200));
    }

    @Test
    public void getV1ItemsShouldReturnTypeApplicationJson() {
        assertThat(itemsTarget.request().get().getMediaType().toString(), is("application/json"));
    }

    @Test
    public void getV1ItemsShouldReturnListOfBooks() throws Exception {
        int size = 3;
        insertBooks(size);
        String json = itemsTarget.request().get(String.class);
        List<Book> actual = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Book.class));
        assertThat(actual.size(), is(size));
    }

    @Test
    public void getV1ItemsShouldStartFromSpecifiedFirstResult() throws Exception {
        insertBooks(10);
        String json = requestItems(5, 0);
        List<Book> actual = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Book.class));
        assertThat(actual.size(), is(5));
    }

    @Test
    public void getV1ItemsShouldReturnSpecifiedMaximumNumberOfBooks() throws Exception {
        insertBooks(10);
        String json = requestItems(0, 7);
        List<Book> actual = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Book.class));
        assertThat(actual.size(), is(7));
    }

    @Test
    public void getV1ItemsShouldReturnSpecifiedMaximumNumberOfBooksAndSkipToTheSpecifiedFirstResult() throws Exception {
        insertBooks(20);
        String json = requestItems(5, 7);
        List<Book> actual = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Book.class));
        assertThat(actual.size(), is(7));
        assertThat(actual.get(0).getId(), is(equalTo(6)));
    }

    @Test
    public void getV1ItemsShouldReturnTheCorrectJson() throws Exception {
        insertBooks(1);
        String json = itemsTarget.request().get(String.class);
        JSONAssert.assertEquals("[{id: 1}]", json, false);
        JSONAssert.assertEquals("[{link: \"/api/v1/items/1\"}]", json, false);
        JSONAssert.assertEquals("[{title: \"" + title + "\"}]", json, false);
    }

    @Test
    public void deleteV1ItemsShouldDeleteAllBooks() {
        itemsTarget.request().delete();
        assertThat(bookDao.getAllBooks().size(), is(0));
    }

    @Test
    public void getV1ItemsIdShouldReturnSpecifiedBook() throws Exception {
        Book expected = insertBooks(1).get(0);
        String json = itemsTarget.path("/" + expected.getId()).request().get(String.class);
        Book actual = objectMapper.readValue(json, Book.class);
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    public void getV1ItemsIdShouldReturnCorrectJson() throws Exception {
        Book expected = insertBooks(1).get(0);
        String json = itemsTarget.path("/" + expected.getId()).request().get(String.class);
        JSONAssert.assertEquals("{id: " + expected.getId() + "}", json, false);
        JSONAssert.assertEquals("{image: \"" + expected.getImage() + "\"}", json, false);
        JSONAssert.assertEquals("{title: \"" + expected.getTitle() + "\"}", json, false);
        JSONAssert.assertEquals("{author: \"" + expected.getAuthor() + "\"}", json, false);
        JSONAssert.assertEquals("{price: " + expected.getPrice() + "}", json, false);
    }

    @Test
    public void putV1ItemsIdShouldSaveNewBook() throws Exception {
        String json = new ObjectMapper().writeValueAsString(book);
        itemsTarget.request().put(Entity.text(json));
        Book actual = bookDao.getBook(id);
        assertThat(actual, is(not(nullValue())));
        assertThat(actual, is(equalTo(book)));
        assertThat(bookDao.getAllBooks().size(), is(1));
    }

    @Test
    public void putV1ItemsShouldUpdateExistingBook() throws Exception {
        bookDao.saveOrUpdateBook(book);
        book.setTitle("This is new title");
        String json = new ObjectMapper().writeValueAsString(book);
        itemsTarget.request().put(Entity.text(json));
        Book actual = bookDao.getBook(id);
        assertThat(actual, is(equalTo(book)));
        assertThat(bookDao.getAllBooks().size(), is(1));
    }

    @Test
    public void deleteV1ItemsIdShouldDeleteExistingBook() throws Exception {
        List<Book> books = insertBooks(3);
        bookDao.saveOrUpdateBook(books.get(0));
        itemsTarget.path("/" + books.get(0).getId()).request().delete();
        assertThat(bookDao.getAllBooks().size(), is(books.size() - 1));
    }

    private String requestItems(int firstResult, int maxResult) {
        if (firstResult > 0) {
            itemsTarget = itemsTarget.queryParam("offset", firstResult);
        }
        if (maxResult > 0) {
            itemsTarget = itemsTarget.queryParam("count", maxResult);
        }
        return itemsTarget.request().get(String.class);
    }

}
