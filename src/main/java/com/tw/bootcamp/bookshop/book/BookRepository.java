package com.tw.bootcamp.bookshop.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByOrderByNameAsc();

    Book findByIsbn(String isbn);

    Book findByIsbn13(String isbn13);

    Book findByIsbn13AndIsbn(String isbn13, String isbn);

    List<Book> findByNameContainsIgnoreCaseOrderByNameAsc(String searchString);
}
