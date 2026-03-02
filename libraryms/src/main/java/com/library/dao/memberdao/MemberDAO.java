package com.library.dao.memberdao;

import java.sql.Timestamp;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.library.db.DBUtil;
import com.library.dto.MemberDTO;

public class MemberDAO implements MemberInter {

    // DTO 객체로 변환
    private MemberDTO mapRow(ResultSet rs) throws SQLException {
        MemberDTO m = new MemberDTO();
        m.setId(rs.getInt("id"));
        m.setPw(rs.getString("password"));
        m.setName(rs.getString("name"));
        m.setAge(rs.getInt("age"));
        m.setEmail(rs.getString("email"));
        m.setPhone(rs.getString("phone"));
        m.setAddress1(rs.getString("address1"));
        m.setAddress2(rs.getString("address2"));
        m.setAdmin(rs.getBoolean("is_admin"));
        m.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null)
            m.setUpdatedAt(updatedTs.toLocalDateTime());

        return m;
    }

    // 회원가입 (회원 정보 추가)
    @Override
    public boolean memberAdd(MemberDTO member) {
        String sql = "INSERT INTO library_member"
                + "(password, name, age, email, phone, address1, address2) "
                + "VALUES (?,?,?,?,?,?,?)";
        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql,
                        Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, member.getPw());
            pstmt.setString(2, member.getName());
            pstmt.setInt(3, member.getAge());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getAddress1());
            pstmt.setString(7, member.getAddress2());

            int result = pstmt.executeUpdate();

            // INSERT 성공 시 자동 생성된 ID를 member 객체에 저장
            if (result > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    member.setId(rs.getInt(1));
                }
            }
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 회원정보 수정
    @Override
    public boolean memberUpdate(MemberDTO member) {
        String sql = "UPDATE library_member "
                + "SET password = ?, name = ?, age = ?, email = ?, phone = ?, address1 = ?, address2 = ?"
                + "WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getPw());
            pstmt.setString(2, member.getName());
            pstmt.setInt(3, member.getAge());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getAddress1());
            pstmt.setString(7, member.getAddress2());
            pstmt.setInt(8, member.getId());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 회원 탈퇴
    @Override
    public boolean memberDel(MemberDTO member) {
        String sql1 = "DELETE FROM book_rental WHERE rental_member_id = ?"; // ✅ 먼저 삭제
        String sql2 = "DELETE FROM library_member WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

            // ✅ 트랜잭션 처리
            conn.setAutoCommit(false);

            pstmt1.setInt(1, member.getId());
            pstmt1.executeUpdate(); // 대여 기록 먼저 삭제

            pstmt2.setInt(1, member.getId());
            boolean result = pstmt2.executeUpdate() > 0; // 회원 삭제

            conn.commit();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 전체 회원 조회
    @Override
    public List<MemberDTO> memberAll() {
        List<MemberDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM library_member";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next())
                list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // id로 회원 조회
    @Override
    public Optional<MemberDTO> memberSearchById(int id) {
        String sql = "SELECT * FROM library_member WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // 로그인
    @Override
    public Optional<MemberDTO> login(int id, String pw) {
        String sql = "SELECT * FROM library_member WHERE id = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, pw);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
