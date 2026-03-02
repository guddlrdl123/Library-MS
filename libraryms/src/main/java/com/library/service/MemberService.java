package com.library.service;

import java.util.List;
import java.util.Optional;

import com.library.dao.memberdao.MemberDAO;
import com.library.dao.memberdao.MemberInter;
import com.library.dto.MemberDTO;

public class MemberService {
    private MemberInter memberDAO = new MemberDAO();

    // 회원가입
    public boolean memberAdd(MemberDTO member) {
        // 이름 null, 공백 값 검증
        if (member.getName() == null || member.getName().isBlank()) {
            System.out.println("이름은 필수 입력 사항입니다.");
            return false;
        }
        // 전화번호 null, 공백 값 검증
        if (member.getPhone() == null || member.getPhone().isBlank()) {
            System.out.println("전화번호는 필수 입력 사항입니다.");
            return false;
        }
        return memberDAO.memberAdd(member);
    }

    // 회원정보 수정
    public boolean memberUpdate(MemberDTO member) {
        return memberDAO.memberUpdate(member);
    }

    // 회원 탈퇴
    public boolean memberDel(MemberDTO member) {
        return memberDAO.memberDel(member);
    }

    // 전체 회원 조회
    public List<MemberDTO> memberAll() {
        return memberDAO.memberAll();
    }

    // id로 회원 조회
    public Optional<MemberDTO> memberSearchById(int id) {
        return memberDAO.memberSearchById(id);
    }

    // 로그인
    public Optional<MemberDTO> login(int id, String pw) {
        Optional<MemberDTO> result = memberDAO.login(id, pw);
        if (result.isEmpty()) {
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return result;
    }
}
