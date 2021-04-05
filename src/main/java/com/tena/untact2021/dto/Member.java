package com.tena.untact2021.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private Integer id;
    private String regDate;
    private String updateDate;
    private String loginId;
    private String loginPw;
    private String name;
    private String nickname;
    private String cellphoneNo;
    private String email;
    private boolean loginStatus;

    @JsonIgnore
    public boolean isLogin() {
        return loginStatus;
    }

    @JsonIgnore
    public boolean isValidInput() {
        return (this.loginPw != null && !this.loginPw.isBlank())
            || (this.name != null && !this.name.isBlank())
            || (this.nickname != null && !this.nickname.isBlank())
            || (this.cellphoneNo != null && !this.cellphoneNo.isBlank());
    }

    // TODO Role 정의 되면 Enum 처리
    /* 관리자 여부 */
    @JsonIgnore
    public Boolean isAdmin() {
        return "admin".equals(this.loginId) || "user1".equals(this.loginId);
        //return this.loginId.equals("admin") || this.loginId.equals("user1"); //null 객체일 경우, equals 메소드 호출이 불가하므로 예외 발생
    }

    @JsonIgnore
    public boolean isSameMember(int writerId) {
        if (this.getId().equals(writerId)) return true;
        return false;
    }

    @JsonIgnore
    public boolean canModify(int writerId) {
        return this.isAdmin() || this.isSameMember(writerId);
    }

    @JsonIgnore
    public boolean canDelete(int writerId) {
        return canModify(writerId);
    }

}