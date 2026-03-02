package com.library.dao.booklistdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.db.DBUtil;
import com.library.dto.BookDTO;
import com.library.dto.Category;

public class BookDAO implements BookInter {

    // DTO 객체로 변환
    private BookDTO mapRow(ResultSet rs) throws SQLException {
        BookDTO b = new BookDTO();
        b.setBookId(rs.getInt("book_id"));
        b.setCategory(Category.valueOf(rs.getString("category")));
        b.setIsbn(rs.getString("isbn"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));

        return b;
    }

    // 책 추가
    @Override
    public boolean bookAdd(BookDTO book) {
        String sql = "INSERT INTO book_list(category, isbn, title, author) VALUES(?,?,?,?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getCategory().name());
            pstmt.setString(2, book.getIsbn());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 책 수정
    @Override
    public boolean bookUpdate(BookDTO book) {
        String sql = "UPDATE book_list SET category = ? , isbn = ?, title = ?, author = ? WHERE book_id = ? ";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getCategory().name());
            pstmt.setString(2, book.getIsbn());
            pstmt.setString(3, book.getTitle());
            pstmt.setString(4, book.getAuthor());
            pstmt.setInt(5, book.getBookId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 책 삭제
    @Override
    public boolean bookDel(BookDTO book) {
        String sql = "DELETE FROM book_list WHERE book_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, book.getBookId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 전체 책 목록
    @Override
    public List<BookDTO> bookList() {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM book_list";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 책 제목으로 조회
    @Override
    public Optional<BookDTO> bookSearchByTitle(String title) {

        // 부분 일치해도 검색 가능
        String sql = "SELECT * FROM book_list WHERE title LIKE ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // title 값만 존재하면 가능
            pstmt.setString(1, "%" + title + "%");
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // 책 ID로 조회
    @Override
    public Optional<BookDTO> bookSearchById(int bookId) {
        String sql = "SELECT * FROM book_list WHERE book_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<BookDTO> bookAvailSearch(int bookId) {
        // avail_book VIEW: 현재 대여 가능한 도서만 담겨있음
        // 결과가 있으면 대여 가능, 없으면 대여 중
        String sql = "SELECT * FROM avail_book WHERE book_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
