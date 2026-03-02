package com.library.dao.booklistdao;

import java.util.List;
import java.util.Optional;

import com.library.dto.BookDTO;

public interface BookInter {

    // 책 추가
    boolean bookAdd(BookDTO book);

    // 책 수정
    boolean bookUpdate(BookDTO book);

    // 책 삭제
    boolean bookDel(BookDTO book);

    // 전체 책 조회
    List<BookDTO> bookList();

    // 책 제목으로 조회
    Optional<BookDTO> bookSearchByTitle(String title);

    // 책 id로 조회
    Optional<BookDTO> bookSearchById(int bookId);

    // avail_book VIEW에서 특정 도서 ID로 조회
    Optional<BookDTO> bookAvailSearch(int bookId);
}
