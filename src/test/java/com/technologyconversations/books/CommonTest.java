package com.technologyconversations.books;

import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class CommonTest {

    protected final int id = 42;
    protected final String image = "http://technologyconversations.com/images/myimage.png";
    protected final String title = "My Life";
    protected final String author = "Viktor Farcic";
    protected final double price = 5.99;
    protected final String link = "/api/v1/items/42";
    protected Book book;
    protected BookDao bookDao = BookDao.getInstance();

    @Before
    public void beforeCommonTest() {
        book = new Book(id);
        book.setImage(image);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
    }

    protected List<Book> insertBooks(int count) {
        List<Book> books = new ArrayList<>();
        for (int index = 1; index <= count; index++) {
            Book book = new Book(index);
            book.setTitle(title);
            book.setAuthor(author);
            book.setImage(image);
            book.setPrice(price);
            bookDao.saveOrUpdateBook(book);
            books.add(book);
        }
        return books;
    }

}
