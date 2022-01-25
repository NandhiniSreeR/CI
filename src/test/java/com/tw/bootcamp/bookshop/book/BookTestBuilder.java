package com.tw.bootcamp.bookshop.book;

public class BookTestBuilder {
    private final Book.BookBuilder bookBuilder;

    public BookTestBuilder() {
        bookBuilder = Book.builder().name("Harry Potter")
                .authorName("J K Rowling")
                .amount(300D)
                .booksCount(5);
    }

    public Book build() {
        return bookBuilder.build();
    }

    public BookTestBuilder withPrice(int price) {
        bookBuilder.amount((double) price);
        return this;
    }

    public BookTestBuilder withName(String name) {
        bookBuilder.name(name);
        return this;
    }

    public BookTestBuilder withId(Long id) {
        bookBuilder.id(id);
        return this;
    }

    public BookTestBuilder withBooksCount(Integer booksCount) {
        bookBuilder.booksCount(booksCount);
        return this;
    }

    public BookTestBuilder withImageUrl(String imageUrl) {
        bookBuilder.imageUrl(imageUrl);
        return this;
    }

    public BookTestBuilder withSmallImageUrl(String smallImageUrl) {
        bookBuilder.smallImageUrl(smallImageUrl);
        return this;
    }
}
