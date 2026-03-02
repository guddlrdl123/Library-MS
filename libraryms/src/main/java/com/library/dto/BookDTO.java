package com.library.dto;

public class BookDTO {
    private int bookId;
    private Category category;
    private String isbn;
    private String title;
    private String author;

    // 생성자
    public BookDTO() {
    }

    public BookDTO(Category category, String isbn, String title, String author) {
        this.category = category;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    // getter, setter
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "[" + category + bookId + "] isbn=" + isbn + ", 제목=" + title
                + ", 작가=" + author;
    }

}
