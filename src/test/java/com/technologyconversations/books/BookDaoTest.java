package com.technologyconversations.books;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class BookDaoTest extends CommonTest {

    @Before
    public void beforeBookDaoTest() {
        bookDao.saveOrUpdateBook(book);
    }

    @After
    public void afterBookDaoTest() {
        bookDao.deleteAllBooks();
    }

    @Test
    public void getAllBooksShouldReturnAllBooks() {
        assertThat(bookDao.getAllBooks().size(), is(equalTo(1)));
    }

    @Test
    public void deleteAllBooksShouldDeleteAllBooks() {
        bookDao.deleteAllBooks();
        assertThat(bookDao.getAllBooks().size(), is(equalTo(0)));
    }

    @Test
    public void getAllBooksShouldReturnsBooksWithId() {
        Book actual = bookDao.getAllBooks().get(0);
        assertThat(actual.getId(), is(equalTo(id)));
    }

    @Test
    public void getAllBooksShouldReturnsBooksWithTitle() {
        Book actual = bookDao.getAllBooks().get(0);
        assertThat(actual.getTitle(), is(equalTo(title)));
    }

    @Test
    public void getAllBooksShouldReturnsBooksWithoutAuthor() {
        Book actual = bookDao.getAllBooks().get(0);
        assertThat(actual.getAuthor(), is(nullValue()));
    }

    @Test
    public void getAllBooksShouldReturnBooksStartingFromSpecifiedFirstResult() {
        int size = 10;
        int firstResult = 5;
        int expected = size - firstResult;
        bookDao.deleteAllBooks();
        insertBooks(size);
        assertThat(bookDao.getAllBooks(firstResult, 0).size(), is(equalTo(expected)));
    }

    @Test
    public void getAllBooksShouldReturnBooksWithSpecifiedMaxResult() {
        int size = 20;
        int maxResult = 10;
        bookDao.deleteAllBooks();
        insertBooks(size);
        assertThat(bookDao.getAllBooks(0, maxResult).size(), is(equalTo(maxResult)));
    }

    @Test
    public void getAllBooksShouldReturnBooksWithSpecifiedMaxResultStartingFromSpecifiedFirstResult() {
        int size = 20;
        int firstResult = 5;
        int maxResult = 10;
        bookDao.deleteAllBooks();
        insertBooks(size);
        List<Book> actual = bookDao.getAllBooks(firstResult, maxResult);
        assertThat(actual.size(), is(equalTo(maxResult)));
        assertThat(actual.get(0).getId(), is(equalTo(firstResult + 1)));
    }

    @Test
    public void getBookShouldReturnBookWithTheSpecifiedId() {
        Book actual = bookDao.getBook(id);
        assertThat(actual, is(equalTo(actual)));
    }

    @Test
    public void getBookShouldReturnNullIfIdDoesNotExist() {
        Book actual = bookDao.getBook(123);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void saveOrUpdateBookShouldSaveTheNewBook() {
        int actualId = 123;
        book.setId(actualId);
        bookDao.saveOrUpdateBook(book);
        assertThat(bookDao.getAllBooks().size(), is(equalTo(2)));
        assertThat(bookDao.getBook(actualId), is(equalTo(book)));
    }

    @Test
    public void saveOrUpdateBookShouldUpdateExistingBook() {
        book.setAuthor("this is new author");
        bookDao.saveOrUpdateBook(book);
        assertThat(bookDao.getAllBooks().size(), is(equalTo(1)));
        assertThat(bookDao.getBook(id), is(equalTo(book)));
    }

    @Test
    public void deleteBookShouldReturnDeletedBook() {
        Book actual = bookDao.deleteBook(id);
        assertThat(actual, is(equalTo(book)));
    }

    @Test
    public void deleteBookShouldReturnNullIfBookDoesNotExist() {
        Book actual = bookDao.deleteBook(123);
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void deleteBookShouldDeleteSpecifiedBook() {
        bookDao.deleteBook(id);
        assertThat(bookDao.getAllBooks().size(), is(equalTo(0)));
    }

}
