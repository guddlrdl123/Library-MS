package com.library.dto;

import java.time.LocalDateTime;

public class MemberDTO {
    private int id;
    private String pw;
    private String name;
    private int age;
    private String email;
    private String phone;
    private String address1;
    private String address2;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isAdmin;

    // 기본 생성자
    public MemberDTO() {
    }

    // 정보 담아올 생성자
    public MemberDTO(String pw, String name, int age, String email,
            String phone, String address1, String address2) {
        this.pw = pw;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.address1 = address1;
        this.address2 = address2;
    }

    // getter, setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "[회원 ID=" + id + ", 이름=" + name + ", 나이=" + age + ", email=" + email
                + ", 전화번호=" + phone + ", 주소=" + address1 + "(" + address2 + "), 회원가입일=" + createdAt
                + ", 회원정보 수정일=" + updatedAt + "]";
    }

}
