package com.library.dao.bookrentaldao;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.db.DBUtil;
import com.library.dto.RentalDTO;

public class RentalDAO implements RentalInter {

    // DAO 객체로 변환
    private RentalDTO mapRow(ResultSet rs) throws SQLException {
        RentalDTO r = new RentalDTO();
        r.setRentalNumber(rs.getInt("rental_number"));
        r.setRentalBookId(rs.getInt("rental_book_id"));
        r.setRentalMemberId(rs.getInt("rental_member_id"));
        r.setRentalDate(rs.getTimestamp("rental_date").toLocalDateTime());
        Timestamp returnTs = rs.getTimestamp("return_date");

        if (returnTs != null) {
            r.setReturnDate(returnTs.toLocalDateTime());
        }
        return r;
    }

    // 대여 추가
    @Override
    public boolean rentalAdd(Connection conn, RentalDTO rental) {
        String sql = "INSERT INTO book_rental(rental_book_id, rental_member_id) VALUES(?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rental.getRentalBookId());
            pstmt.setInt(2, rental.getRentalMemberId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 기간 연장(2주)
    @Override
    public boolean rentalUpdate(Connection conn, RentalDTO rental) {
        // 빌린 기간에 14일 더하기
        String sql = "UPDATE book_rental "
                + "SET rental_date = DATE_ADD(rental_date, INTERVAL 14 DAY) "
                + "WHERE rental_number = ? AND return_date IS NULL"; // 반납된 책은 연장 불가

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rental.getRentalNumber());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 반납
    @Override
    public boolean returnBook(Connection conn, RentalDTO rental) {
        String sql = "UPDATE book_rental SET return_date = NOW() "
                + "WHERE rental_number = ? AND return_date IS NULL"; // 중복 반납 방지 null 체크

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rental.getRentalNumber());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 전체 대여 목록
    @Override
    public List<RentalDTO> rentalAll() {
        List<RentalDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM book_rental";

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

    // id로 대여 기록 조회
    @Override
    public Optional<RentalDTO> rentalSearchById(int memberId) {
        String sql = "SELECT * FROM book_rental "
                + "WHERE rental_member_id = ? AND return_date IS NULL"; // 반납된 기록 제외

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // overdue 전용 매핑
    private RentalDTO mapOverdueRow(ResultSet rs) throws SQLException {
        RentalDTO r = new RentalDTO();
        r.setRentalNumber(rs.getInt("rental_number"));
        r.setMemberName("member_name");
        r.setBookTitle(rs.getString("book_title"));
        r.setRentalDate(rs.getTimestamp("rental_date").toLocalDateTime());
        r.setOverdueDays(rs.getInt("overdue_days"));
        return r;
    }

    // (관리자 전용) 연체된 회원 목록 조회
    @Override
    public List<RentalDTO> returnLateMember() {
        List<RentalDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM overdue";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapOverdueRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
