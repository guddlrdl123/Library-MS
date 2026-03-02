package com.library.dao.memberdao;

import java.util.List;
import java.util.Optional;

import com.library.dto.MemberDTO;

public interface MemberInter {

    // 회원가입 (회원 정보 추가)
    boolean memberAdd(MemberDTO member);

    // 회원정보 수정
    boolean memberUpdate(MemberDTO member);

    // 회원 탈퇴
    boolean memberDel(MemberDTO member);

    // 전체 회원 조회
    List<MemberDTO> memberAll();

    // id로 회원 조회
    Optional<MemberDTO> memberSearchById(int id);

    // 로그인
    Optional<MemberDTO> login(int id, String pw);
}