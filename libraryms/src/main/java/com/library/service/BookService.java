package com.library.service;

import java.util.List;
import java.util.Optional;

import com.library.dao.booklistdao.BookDAO;
import com.library.dao.booklistdao.BookInter;
import com.library.dto.BookDTO;

public class BookService {
    private BookInter bookDAO = new BookDAO();

    // 도서 추가
    public boolean bookAdd(BookDTO book) {
        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            System.out.println("isbn은 필수 입력 사항입니다.");
            return false;
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            System.out.println("제목은 필수 입력 사항입니다.");
            return false;
        }
        return bookDAO.bookAdd(book);
    }

    // 도서 추가
    public boolean bookUpdate(BookDTO book) {
        return bookDAO.bookUpdate(book);
    }

    // 도서 삭제
    public boolean bookDel(BookDTO book) {
        return bookDAO.bookDel(book);
    }

    // 전체 도서 조회
    public List<BookDTO> bookList() {
        return bookDAO.bookList();
    }

    // 제목으로 도서 조회
    public Optional<BookDTO> bookSearchByTitle(String title) {
        Optional<BookDTO> result = bookDAO.bookSearchByTitle(title);
        if (result.isEmpty()) {
            System.out.println("찾으시는 정보가 없습니다");
        }
        return result;
    }

    // 책 ID로 도서 조회
    public Optional<BookDTO> bookSearchById(int bookId) {
        return bookDAO.bookSearchById(bookId);
    }

    // bookId로 대여 가능 여부 확인
    public Optional<BookDTO> bookAvailSearch(int bookId) {
        return bookDAO.bookAvailSearch(bookId);
    }

}
