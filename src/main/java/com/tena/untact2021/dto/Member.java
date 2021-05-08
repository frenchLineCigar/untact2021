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

    @JsonIgnore
    private String loginPw;

    @JsonIgnore
    private int authLevel; // 권한

    @JsonIgnore
    private String authKey;

    @JsonIgnore
    private AuthKeyStatus authKeyStatus;

    private String name;

    private String nickname;

    private String cellphoneNo;

    private String email;

    @JsonIgnore
    private boolean loginStatus;

    public enum AuthKeyStatus {
        INVALID, VALID, NONE;
    }

    @JsonIgnore
    public boolean isLogin() {
        return loginStatus;
    }

    @JsonIgnore
    public boolean isValidInput() {
        return (this.loginPw != null && !this.loginPw.isBlank())
            || (this.name != null && !this.name.isBlank())
            || (this.nickname != null && !this.nickname.isBlank())
            || (this.cellphoneNo != null && !this.cellphoneNo.isBlank())
            || (this.email != null && !this.email.isBlank());
    }

    /* 관리자 여부 */
    @JsonIgnore
    public boolean isAdmin() {
        return this.authLevel == AuthLevel.ADMIN.getRoleNo();
    }

    @JsonIgnore
    public boolean isSameMember(int writerId) {
        return this.getId() == writerId;
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