# Library-MS
JAVA &amp; JDBC 기반 도서 관리 시스템

# 개발 환경
- Java 21
- MySQL 8.3
- Maven

## 주요 기능
- 기본 메뉴(로그인, 회원가입) 구현
- 회원과 관리자 페이지를 구분 후 구현
- 회원의 개인정보가 나오는 메뉴는 비밀번호 입력 후 사용 가능한 검증 추가

# 회원 페이지의 기능
- 전체 도서 목록 조회
- 책 제목의 키워드를 통한 도서 검색
- 도서 대여/반납
- 대여 기간 연장
- 연체됐을 시에 연체료 계산
- 나의 대여 현황
- 회원 정보 수정
- 내 정보
- 탈퇴
- 로그아웃

# 관리자 페이지의 기능
- 전체 도서 목록 조회
- 도서 등록/수정/삭제
- 전체 회원 목록 조회
- 특정 회원ID로 조회
- 연체된 회원 목록 조회
- 전체 대여 목록 조회
- 로그아웃

## 데이터 베이스 스키마

# libraryms 데이터 베이스 생성
create database libraryms;
use libraryms;

# library_member 테이블 생성
create table library_member(
	id int primary key auto_increment,
	password varchar(20) not null,
	name varchar(50) not null,
	age int not null check(age>=8),
	email varchar(100) unique,
	phone varchar(20) not null,
	address1 varchar(100),
	address2 varchar(100),
	created_at datetime default now(),
	updated_at datetime default null,
	is_admin BOOLEAN NOT NULL DEFAULT FALSE
);

-- 관리자 계정 추가
insert into library_member (password, name, age, email, phone, is_admin) 
values ('manager', '관리자', 99, 'manager@admin.com', '000-0000-0000', true);

# book_list 테이블 추가
create table book_list(
	book_id int primary key auto_increment,
	category enum('B', 'M', 'D', 'ETC') default 'ETC',
	-- B: 책, M: 잡지, D: 사전, ETC: 기타
	isbn varchar(17) not null unique,
	title varchar(100) not null,
	author varchar(50) not null
);

# book_rental 테이블 추가
- library_member의 id와 book_list의 book_id 참조

create table book_rental(
	rental_number int primary key auto_increment,
	rental_book_id int not null,
	rental_member_id int not null,
	rental_date datetime default now(),
	return_date datetime default null,
	foreign key(rental_book_id) references book_list(book_id)
	on delete restrict on update cascade, -- 대여 기록 있을 시 삭제X, 수정 시 rental_id도 같이 변경
	foreign key(rental_member_id) references library_member(id)
	on delete restrict on update cascade
);

# 기능에 필요한 view 추가
- 대여 중임을 알 수 있는 rented_book
- 대여 가능함을 알 수 있는 avail_book
- 연체 중인 책과 회원을 알 수 있는 overdue

-- 현재 대여 중인 책 (rented_book)
create view rented_book as
select bl.*
from book_list bl
where exists(
	select 1 from book_rental br
	where br.rental_book_id = bl.book_id and return_date is null
);

-- 대여 가능한 책 (avail_book)
create view avail_book as
select bl.*
from book_list bl
where not exists(
	select 1 from book_rental br
	where br.rental_book_id = bl.book_id and return_date is null
);

-- 연체 중인 책, 회원 목록
create view overdue as
select
    br.rental_number,
    m.name  as member_name,
    bl.title as book_title,
    br.rental_date,
    datediff(now(), br.rental_date) as overdue_days
from book_rental br
join library_member m  on br.rental_member_id = m.id
join book_list bl      on br.rental_book_id   = bl.book_id
where br.return_date is null
  and datediff(now(), br.rental_date) > 14;

## 회원 정보 수정 시에 자동으로 일자 갱신 trigger
-- 회원 정보 수정 일자 자동 갱신
create trigger update_member_timestamp
before update on library_member
for each row
set new.updated_at = now();

# 샘플 데이터 추가
INSERT INTO book_list (category, isbn, title, author) VALUES
('B','9780000000001','자바의 정석','남궁성'),
('B','9780000000002','이것이 자바다','신용권'),
('B','9780000000003','스프링 입문','김영한'),
('B','9780000000004','객체지향의 사실과 오해','조영호'),
('B','9780000000005','클린 코드','Robert C. Martin'),
('B','9780000000006','모던 자바 인 액션','라울-게이브리얼 우르마'),
('B','9780000000007','Effective Java','Joshua Bloch'),
('B','9780000000008','데이터베이스 개론','김연희'),
('B','9780000000009','운영체제의 이해','이상호'),
('B','9780000000010','자료구조와 알고리즘','천인국'),
('M','9780000000011','월간 개발자 1월호','개발자협회'),
('M','9780000000012','월간 개발자 2월호','개발자협회'),
('M','9780000000013','코딩 매거진 봄호','IT출판사'),
('M','9780000000014','코딩 매거진 여름호','IT출판사'),
('M','9780000000015','AI 트렌드 2025','테크저널'),
('D','9780000000016','영한 사전','사전출판사'),
('D','9780000000017','한영 사전','사전출판사'),
('D','9780000000018','컴퓨터 용어 사전','IT용어연구소'),
('D','9780000000019','프로그래밍 용어 사전','개발연구소'),
('D','9780000000020','경제 용어 사전','경제출판사'),
('ETC','9780000000021','자기계발의 기술','홍길동'),
('ETC','9780000000022','시간 관리의 비밀','이철수'),
('ETC','9780000000023','습관의 힘','찰스 두히그'),
('ETC','9780000000024','부자의 생각법','김부자'),
('ETC','9780000000025','미래를 읽는 법','박미래'),
('B','9780000000026','리팩토링','Martin Fowler'),
('B','9780000000027','도메인 주도 설계','Eric Evans'),
('B','9780000000028','HTTP 완벽 가이드','데이비드 고울리'),
('B','9780000000029','네트워크 첫걸음','미즈구치 카츠야'),
('B','9780000000030','리눅스 시스템 프로그래밍','로버트 러브'),
('M','9780000000031','월간 과학 3월호','과학협회'),
('M','9780000000032','월간 과학 4월호','과학협회'),
('M','9780000000033','테크 리뷰 가을호','테크리뷰'),
('M','9780000000034','테크 리뷰 겨울호','테크리뷰'),
('M','9780000000035','보안 매거진','보안연구소'),
('D','9780000000036','법률 용어 사전','법률출판사'),
('D','9780000000037','의학 용어 사전','의학출판사'),
('D','9780000000038','건축 용어 사전','건축연구소'),
('D','9780000000039','철학 용어 사전','철학출판사'),
('D','9780000000040','문학 용어 사전','문학출판사'),
('ETC','9780000000041','여행의 이유','김영하'),
('ETC','9780000000042','아침의 피아노','김진영'),
('ETC','9780000000043','사피엔스','유발 하라리'),
('ETC','9780000000044','코스모스','칼 세이건'),
('ETC','9780000000045','총 균 쇠','재레드 다이아몬드'),
('B','9780000000046','알고리즘 문제 해결 전략','구종만'),
('B','9780000000047','토비의 스프링','이일민'),
('B','9780000000048','JPA 프로그래밍','김영한'),
('B','9780000000049','쿠버네티스 완벽 가이드','나이젤 폴턴'),
('B','9780000000050','파이썬 머신러닝','세바스찬 라시카');
