package io.sixhours.memecached.demo;

import java.util.List;

/**
 * The interface of Book repository.
 *
 * @author Sasa Bolic
 */
public interface BookRepository {

    /**
     * Returns list of all books.
     *
     * @return the list
     */
    List<Book> findAll();

    /**
     * Returns list of books containing given {@code title}.
     *
     * @param title the title
     * @return the list
     */
    List<Book> findByTitle(String title);

    /**
     * Delete book with given title and re-cache list of books.
     *
     * @return the list
     */
    List<Book> deleteAndRecache(String title);
}
