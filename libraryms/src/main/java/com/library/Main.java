package com.library;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.library.dto.*;
import com.library.service.*;

// 시스템 실행 클래스
public class Main {
    private static final Scanner sc = new Scanner(System.in);

    // 각각의 기능(메서드)들이 담긴 Service 객체 생성
    private static final MemberService memberService = new MemberService();
    private static final BookService bookService = new BookService();
    private static final RentalService rentalService = new RentalService();

    // 로그인한 회원 정보를 담기 위한 변수 선언
    private static MemberDTO loginMember = null;

    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("                     [도서 관리 프로그램]");
        System.out.println("================================================================");

        while (true) {
            if (loginMember == null) {
                guestMenu(); // 기본 메뉴
            } else if (loginMember.isAdmin()) {
                showAdminMenu(); // 관리자 메뉴
            } else {
                memberMenu(); // 회원 메뉴
            }
        }
    }

    // 기본 메뉴
    private static void guestMenu() {
        System.out.println("\n [메뉴] 1.로그인 2.회원가입 0.종료");
        System.out.println("선택 > ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                memberAdd();
                break;
            case 0:
                System.out.println("프로그램을 종료합니다.");
                sc.close();
                System.exit(0);

            default:
                System.out.println("올바르지 않은 값 입력.");
        }

    }

    // 회원 메뉴
    private static void memberMenu() {
        System.out.println("\n안녕하세요, " + loginMember.getName() + "님!");
        System.out.println("[메뉴] 1.도서목록  2.도서검색  3.대여  4.반납  5.연장  6.내 대여현황  7.회원정보수정  8.내 정보  9.회원탈퇴  0.로그아웃");
        System.out.print("선택 > ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                bookList();
                break;
            case 2:
                bookSearchByTitle();
                break;
            case 3:
                rentalAdd();
                break;
            case 4:
                returnBook();
                break;
            case 5:
                rentalUpdate();
                break;
            case 6:
                myRental();
                break;
            case 7:
                memberUpdate();
                break;
            case 8:
                myInfo();
                break;
            case 9:
                memberDel();
                break;
            case 0:
                System.out.println("로그아웃 되었습니다.");
                loginMember = null;
                break;
            default:
                System.out.println("올바르지 않은 값 입력.");
        }
    }

    // 관리자 메뉴
    private static void showAdminMenu() {
        System.out.println("\n관리자님! 어떤 메뉴로 이동하시겠습니까?");
        System.out.println("[메뉴] 1.도서목록  2.도서등록  3.도서수정  4.도서삭제  5.전체회원조회  6.회원ID조회  7.연체회원조회  8.전체대여목록  0.로그아웃");
        System.out.print("선택 > ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                bookList();
                break;
            case 2:
                bookAdd();
                break;
            case 3:
                bookUpdate();
                break;
            case 4:
                bookDel();
                break;
            case 5:
                memberAll();
                break;
            case 6:
                memberSearchById();
                break;
            case 7:
                returnLateMember();
                break;
            case 8:
                rentalAll();
                break;
            case 0:
                System.out.println("로그아웃 되었습니다.");
                loginMember = null;
                break;
            default:
                System.out.println("올바르지 않은 값 입력.");
        }
    }

    // ===MEMBER 기능==============================================================

    // 로그인
    private static void login() {
        System.out.println("ID 입력 > ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.println("비밀번호 입력 >");
        String pw = sc.nextLine();

        // 빈값 검증 후 출력
        Optional<MemberDTO> result = memberService.login(id, pw);
        result.ifPresent(m -> {
            loginMember = m;
            System.out.println("환영합니다, " + m.getName() + "님");
        });
    }

    // 회원가입
    private static void memberAdd() {
        System.out.println("\n ----[회원가입]----");
        System.out.printf("비밀번호 > ");
        String pw = sc.nextLine();
        System.out.printf("이름 > ");
        String name = sc.nextLine();
        System.out.printf("나이 > ");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.printf("이메일 > ");
        String email = sc.nextLine();
        System.out.printf("전화번호 > ");
        String phone = sc.nextLine();
        System.out.printf("주소 > ");
        String address1 = sc.nextLine();
        System.out.printf("상세주소 > ");
        String address2 = sc.nextLine();

        MemberDTO member = new MemberDTO(pw, name, age, email, phone, address1, address2);

        boolean result = memberService.memberAdd(member);

        if (result) {
            System.out.println("회원가입이 완료되었습니다. 환영합니다.");
            System.out.println("회원님의 id는 [" + member.getId() + "]입니다.");
        } else {
            System.out.println("회원가입 실패");
        }
    }

    private static void memberUpdate() {
        System.out.println("수정을 위한 비밀번호 확인");
        System.out.println("비밀번호 > ");
        String pw = sc.nextLine();
        if (pw.equals(loginMember.getPw())) {
            System.out.println("\n----[회원정보 수정]----");
            System.out.print("비밀번호 > ");
            String password = sc.nextLine();
            System.out.print("이름 > ");
            String name = sc.nextLine();
            System.out.print("나이 > ");
            int age = sc.nextInt();
            sc.nextLine();
            System.out.print("이메일 > ");
            String email = sc.nextLine();

            System.out.print("전화번호 > ");
            String phone = sc.nextLine();
            System.out.print("주소 > ");
            String address1 = sc.nextLine();
            System.out.print("상세주소 > ");
            String address2 = sc.nextLine();

            loginMember.setName(name);
            loginMember.setAge(age);
            loginMember.setEmail(email);
            loginMember.setPw(password);
            loginMember.setPhone(phone);
            loginMember.setAddress1(address1);
            loginMember.setAddress2(address2);

            boolean result = memberService.memberUpdate(loginMember);
            System.out.println(result ? "회원정보 수정 완료!" : "수정 실패");
        } else {
            System.out.println("비밀번호가 틀렸습니다.");
        }
    }

    // 회원 탈퇴
    private static void memberDel() {
        System.out.println("\n----[회원 탈퇴]----");
        System.out.println("정말 탈퇴하시겠습니까? (y/n)");
        String c = sc.nextLine();
        if (c.toLowerCase().trim().charAt(0) != 'y') {
            System.out.println("취소되었습니다.");
            return;
        }
        boolean result = memberService.memberDel(loginMember);
        if (result) {
            System.out.println("탈퇴가 완료되었습니다.");
            guestMenu();
        } else {
            System.out.println("탈퇴 실패");
        }
    }

    // 내 정보 조회
    private static void myInfo() {

        System.out.println("정보 조회를 위해 비밀번호를 입력해 주세요.");
        String insertPw = sc.nextLine();
        if (!insertPw.equals(loginMember.getPw())) {
            System.out.println("비밀번호가 일치하지 않습니다.");
            return;
        }

        Optional<RentalDTO> myRental = rentalService.rentalSearchById(loginMember.getId());
        System.out.println("\n----[내 정보 조회]----");
        System.out.println("\n── 회원 정보 ──");
        System.out.println("ID       : " + loginMember.getId());
        System.out.println("이름     : " + loginMember.getName());
        System.out.println("나이     : " + loginMember.getAge());
        System.out.println("이메일   : " + loginMember.getEmail());
        System.out.println("전화번호 : " + loginMember.getPhone());
        System.out.println("주소     : " + loginMember.getAddress1() + " (" + loginMember.getAddress2() + ")");

        if (myRental.isPresent()) {
            RentalDTO r = myRental.get();
            System.out.println("\n----[현재 대여 중인 도서]----");
            System.out.println("대여번호 : " + r.getRentalNumber());
            System.out.println("도서 ID  : " + r.getRentalBookId());
            System.out.println("대여일   : " + r.getRentalDate());
        } else {
            System.out.println("현재 대여 중인 도서가 없습니다.");
        }

    }

    // (관리자 전용)id로 회원 조회
    private static void memberSearchById() {
        System.out.println("\n----[특정 회원 조회]----");
        System.out.println("검색하실 회원 ID 입력 > ");
        int id = sc.nextInt();

        Optional<MemberDTO> result = memberService.memberSearchById(id);

        if (result.isEmpty()) {
            System.out.println("찾으시는 회원이 없습니다.");
            return;
        }

        MemberDTO m = result.get();
        System.out.println("\n── 회원 정보 ──");
        System.out.println("ID       : " + m.getId());
        System.out.println("이름     : " + m.getName());
        System.out.println("나이     : " + m.getAge());
        System.out.println("이메일   : " + m.getEmail());
        System.out.println("전화번호 : " + m.getPhone());
        System.out.println("주소     : " + m.getAddress1() + " (" + m.getAddress2() + ")");
    }

    // (관리자 전용)전체 회원 조회
    private static void memberAll() {
        List<MemberDTO> list = memberService.memberAll();
        if (list.isEmpty()) {
            System.out.println("등록된 회원이 없습니다.");
            return;
        }
        System.out.println("\n----[전체 회원 목록]----");
        for (MemberDTO m : list) {
            System.out.println(m + "\n");
        }
    }

    // ===BOOK 기능==============================================================

    // 전체 도서 목록
    private static void bookList() {
        List<BookDTO> list = bookService.bookList();

        // 값 검증
        if (list.isEmpty()) {
            System.out.println("등록된 도서가 없습니다.");
            return;
        }
        System.out.println("\n----[전체 도서 목록]----");
        for (BookDTO b : list) {
            System.out.println(b + "\n");
        }
    }

    // 제목으로 도서 검색
    private static void bookSearchByTitle() {
        System.out.println("찾으시는 도서명을 입력해 주세요. \n >");
        String title = sc.nextLine();
        Optional<BookDTO> result = bookService.bookSearchByTitle(title);
        result.ifPresentOrElse(
                b -> System.out.printf("[%s%d]제목: %s, 작가: %s%n",
                        b.getCategory(), b.getBookId(), b.getTitle(), b.getAuthor()),
                () -> System.out.println("찾으시는 도서가 없습니다."));
    }

    // (관리자 전용) 도서 등록
    private static void bookAdd() {
        System.out.println("\n----[도서 등록]----");
        System.out.printf("카테고리[B(책) / M(잡지) / D(사전) / ETC(기타)] >");
        String categoryStr = sc.nextLine().toUpperCase();
        System.out.printf("ISBN >");
        String isbn = sc.nextLine();
        System.out.printf("제목 >");
        String title = sc.nextLine();
        System.out.printf("작가 >");
        String author = sc.nextLine();

        Category category;
        try {
            category = Category.valueOf(categoryStr);
        } catch (IllegalArgumentException e) {
            System.out.println("올바른 값을 입력해 주세요." + e.getMessage());
            return;
        }
        BookDTO book = new BookDTO(category, isbn, title, author);
        boolean result = bookService.bookAdd(book);
        System.out.println(result ? "도서 등록 완료" : "도서 등록 실패");

    }

    // (관리자 전용)도서 수정
    private static void bookUpdate() {
        System.out.println("\n----[도서 수정]----");
        System.out.println("수정할 도서 ID >");
        int bookId = sc.nextInt();
        sc.nextLine();

        Optional<BookDTO> search = bookService.bookSearchById(bookId);
        if (search.isEmpty()) {
            System.out.println("찾으시는 도서가 없습니다.");
            return;
        }

        System.out.print("카테고리(B/M/D/ETC) > ");
        String categoryStr = sc.nextLine().toUpperCase();
        System.out.print("ISBN > ");
        String isbn = sc.nextLine();
        System.out.print("제목 > ");
        String title = sc.nextLine();
        System.out.print("저자 > ");
        String author = sc.nextLine();

        Category category;
        try {
            category = Category.valueOf(categoryStr);
        } catch (IllegalArgumentException e) {
            System.out.println("올바른 값을 입력해 주세요." + e.getMessage());
            return;
        }
        BookDTO book = new BookDTO(category, isbn, title, author);
        book.setBookId(bookId);
        boolean result = bookService.bookUpdate(book);
        System.out.println(result ? "도서 수정 완료" : "도서 수정 실패");
    }

    // (관리자 전용)도서 삭제
    private static void bookDel() {
        System.out.println("\n----[도서 삭제]----");
        System.out.println("삭제할 도서 ID >");
        int bookId = sc.nextInt();
        sc.nextLine();
        System.out.println("정말 삭제하시겠습니까?(y/n)");
        String del = sc.nextLine();

        if (del.toLowerCase().charAt(0) != 'y') {
            System.out.println("삭제가 취소되었습니다.");
            return;
        }

        BookDTO book = new BookDTO();
        book.setBookId(bookId);
        boolean result = bookService.bookDel(book);
        System.out.println(result ? "삭제 완료" : "삭제 실패(대여 중인 책이거나 올바르지 않은 값입니다.)");
    }

    // ===RENTAL 기능==============================================================

    // 대여
    private static void rentalAdd() {
        System.out.println("\n----[도서 대여]----");
        System.out.print("대여할 도서 ID >");
        int bookId = sc.nextInt();
        sc.nextLine();

        // 이미 대여 중인 책인지 확인
        Optional<BookDTO> availBook = bookService.bookAvailSearch(bookId);
        if (availBook.isEmpty()) {
            System.out.println("이미 대여 중인 도서입니다.");
            return;
        }

        RentalDTO rental = new RentalDTO(bookId, loginMember.getId());
        boolean result = rentalService.rentalAdd(rental);
        if (result) {

            int rentalNumber = rentalService.rentalSearchById(loginMember.getId())
                    .get()
                    .getRentalNumber();
            System.out.println("대여가 완료되었습니다. 대여 번호는 " + rentalNumber + "입니다.");
        } else {
            System.out.println("대여 실패!");
        }
    }

    // 연체일 계산 (대여일로부터 14일 초과한 일수 반환)
    private static long calcOverdueDays(RentalDTO rental) {
        LocalDateTime dueDate = rental.getRentalDate().plusDays(14); // 반납 기한 = 대여일 + 14일
        LocalDateTime now = LocalDateTime.now(); // 현재 시각
        if (now.isAfter(dueDate)) {
            // 현재 시각이 반납 기한을 넘었으면 초과일수 계산
            return java.time.Duration.between(dueDate, now).toDays();
        }
        return 0; // 기한 내 반납이면 0 반환
    }

    // 반납
    private static void returnBook() {
        System.out.println("\n----[도서 반납]----");
        System.out.println("대여 번호 >");
        int rentalNum = sc.nextInt();
        sc.nextLine();

        Optional<RentalDTO> myRental = rentalService.rentalSearchById(loginMember.getId());
        if (myRental.isEmpty()) {
            System.out.println("대여 중인 도서가 없습니다.");
            return;
        }
        if (myRental.get().getRentalNumber() != rentalNum) {
            System.out.println("해당 번호는 당신의 대여 기록에 없습니다.");
            return;
        }

        RentalDTO rental = myRental.get();

        // 카테고리 확인용 로직
        // if (bookService.bookAvailSearch(rental.getRentalBookId()).isEmpty()) {
        // System.out.println("도서 정보를 찾을 수 없습니다. 1");
        // }
        // bookID로 존재 여부 검증 (값이 있으면 객체 반환, 없으면 null 반환)
        BookDTO bookInfo = bookService.bookSearchById(rental.getRentalBookId())
                .orElse(null);

        if (bookInfo == null) {
            System.out.println("도서 정보를 찾을 수 없습니다.");
            return;
        }

        long overdueDays = calcOverdueDays(rental);
        if (overdueDays > 0) {
            int feePerDay = bookInfo.getCategory().getFeePerDay();
            long totalFee = overdueDays * feePerDay;
            System.out.println("연체 " + overdueDays + "일 초과");
            System.out.println("연체료 : " + totalFee + "원 ("
                    + bookInfo.getCategory() + " 카테고리 " + feePerDay + "원/일)");
            System.out.print("연체료 납부 후 반납하시겠습니까? (y/n) > ");
            String answer = sc.nextLine();
            if (answer.toLowerCase().charAt(0) != 'y') {
                System.out.println("납부부터 하세요. 이자 붙입니다 이제");
                return;
            }
        }
        boolean result = rentalService.returnBook(rental);
        if (result) {
            System.out.println("반납이 완료되었습니다.");
        } else {
            System.out.println("반납 처리에 실패했습니다.");
        }
    }

    // 대여 기간 연장
    private static void rentalUpdate() {
        System.out.println("\n----[대여 기간 연장]----");
        System.out.println("연장할 대여 번호 >");
        int rentalNum = sc.nextInt();
        sc.nextLine();

        Optional<RentalDTO> myRental = rentalService.rentalSearchById(loginMember.getId());
        if (myRental.isEmpty()) {
            System.out.println("현재 대여 중인 기록이 없습니다.");
            return;
        }
        if (myRental.get().getRentalNumber() != rentalNum) {
            System.out.println("본인의 대여 기록만 연장 가능합니다.");
            return;
        }

        RentalDTO rental = new RentalDTO();
        rental.setRentalNumber(rentalNum);
        boolean result = rentalService.rentalUpdate(rental);
        if (result) {
            LocalDateTime rentalDay = rentalService.rentalSearchById(loginMember.getId())
                    .get().getRentalDate();
            System.out.println("대여일이 14일 연장 되었습니다. " + rentalDay + "까지 반납해 주시면 됩니다.");
        } else {
            System.out.println("연장 실패!");
        }

    }

    // 내 대여 현황
    private static void myRental() {
        System.out.println("\n----[나의 대여 목록]----");
        Optional<RentalDTO> result = rentalService.rentalSearchById(loginMember.getId());
        result.ifPresentOrElse(
                r -> System.out.printf("대여번호: %d, 도서ID: %d, 대여일: %s%n", r.getRentalNumber(), r.getRentalBookId(),
                        r.getRentalDate()),
                () -> System.out.println("현재 대여 중인 도서가 없습니다."));
    }

    // (관리자 전용)전체 대여 목록
    private static void rentalAll() {
        System.out.println("\n----[전체 대여 목록]----");
        List<RentalDTO> list = rentalService.rentalAll();

        if (list.isEmpty()) {
            System.out.println("회원들의 대여 기록이 없습니다.");
            return;
        }
        for (RentalDTO r : list) {
            System.out.println(r + "\n");
        }
    }

    // (관리자 전용)연체된 회원 리스트
    private static void returnLateMember() {
        System.out.println("\n----[연체된 회원 목록]----");

        List<RentalDTO> list = rentalService.returnLateMember(loginMember);
        if (list.isEmpty()) {
            System.out.println("연체 중인 회원이 없습니다.");
            return;
        }
        for (RentalDTO r : list) {
            System.out.println(r + "\n");
        }
    }
}