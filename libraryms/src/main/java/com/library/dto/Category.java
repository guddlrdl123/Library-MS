package com.library.dto;

// enum 타입 catrgory 관리 (B: 책, M: 잡지, D: 사전, etc: 기타)
public enum Category {
    // () 는 하루당 연체료
    B(500),
    M(1000),
    D(1500),
    ETC(300);

    private final int feePerDay;

    Category(int feePerDay) {
        this.feePerDay = feePerDay;
    }

    public int getFeePerDay() {
        return feePerDay;
    }
}
