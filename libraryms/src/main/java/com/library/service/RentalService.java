package com.library.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.library.dao.bookrentaldao.RentalDAO;
import com.library.dao.bookrentaldao.RentalInter;
import com.library.db.DBUtil;
import com.library.dto.MemberDTO;
import com.library.dto.RentalDTO;

public class RentalService {
    private RentalInter rentalDAO = new RentalDAO();

    // 대여 실패시 rollback, 성공시 commit
    public boolean rentalAdd(RentalDTO rental) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            boolean result = rentalDAO.rentalAdd(conn, rental);
            if (!result) {
                conn.rollback();
                System.out.println("대여에 실패했습니다.");
                return false;
            }

            conn.commit();
            System.out.println("대여 완료");
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 대여 기간 연장 실패시 rollback, 성공시 commit
    public boolean rentalUpdate(RentalDTO rental) {
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            boolean result = rentalDAO.rentalUpdate(conn, rental);
            if (!result) {
                System.out.println("기간 연장 실패(이미 반납되었거나 존재하지 않는 번호입니다.");
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.getMessage();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 반납 실패시 rollback, 성공시 commit
    public boolean returnBook(RentalDTO rental) {
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            boolean result = rentalDAO.returnBook(conn, rental);
            if (!result) {
                conn.rollback();
                System.out.println("반납 실패 (이미 반납되었거나 존재하지 않는 번호입니다.)");
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 전체 대여 목록 조회
    public List<RentalDTO> rentalAll() {
        return rentalDAO.rentalAll();
    }

    // 회원id로 대여 조회
    public Optional<RentalDTO> rentalSearchById(int memberId) {
        return rentalDAO.rentalSearchById(memberId);
    }

    // (관리자 전용) 연체 회원 리스트
    public List<RentalDTO> returnLateMember(MemberDTO loginMember) {
        if (!loginMember.isAdmin()) {
            return List.of(); // 빈 리스트 반환
        }
        return rentalDAO.returnLateMember();
    }
}
