package com.library.dao.bookrentaldao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import com.library.dto.RentalDTO;

public interface RentalInter {

    // 대여 추가
    boolean rentalAdd(Connection conn, RentalDTO rental);

    // 기간 연장(2주)
    boolean rentalUpdate(Connection conn, RentalDTO rental);

    // 반납
    boolean returnBook(Connection conn, RentalDTO rental);

    // 전체 대여 목록
    List<RentalDTO> rentalAll();

    // member_id로 빌린 책 검색
    Optional<RentalDTO> rentalSearchById(int memberId);

    // (관리자 전용) 연체 회원 조회
    List<RentalDTO> returnLateMember();
}
