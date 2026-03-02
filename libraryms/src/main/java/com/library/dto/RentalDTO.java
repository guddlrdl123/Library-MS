package com.library.dto;

import java.time.LocalDateTime;

public class RentalDTO {
    private int rentalNumber;
    private int rentalBookId;
    private int rentalMemberId;
    private int overdueDays;
    private String memberName;
    private String bookTitle;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;

    // 생성자
    public RentalDTO() {
    }

    public RentalDTO(int rentalBookId, int rentalMemberId) {
        this.rentalBookId = rentalBookId;
        this.rentalMemberId = rentalMemberId;
    }

    // getter, setter
    public int getRentalNumber() {
        return rentalNumber;
    }

    public void setRentalNumber(int rentalNumber) {
        this.rentalNumber = rentalNumber;
    }

    public int getRentalBookId() {
        return rentalBookId;
    }

    public void setRentalBookId(int rentalBookId) {
        this.rentalBookId = rentalBookId;
    }

    public int getRentalMemberId() {
        return rentalMemberId;
    }

    public void setRentalMemberId(int rentalMemberId) {
        this.rentalMemberId = rentalMemberId;
    }

    public LocalDateTime getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDateTime rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    @Override
    public String toString() {
        return "[대여 번호=" + rentalNumber + ", 대여 도서 ID=" + rentalBookId + ", 대여한 회원 ID="
                + rentalMemberId + ", 대여일=" + rentalDate + ", 반납입=" + (returnDate == null ? "대여중" : returnDate) + "]";
    }
}
