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

    // TODO 인터셉터 처리
    /* 게시물 수정 가능 */
    public boolean canModify(Article article) {
        // 작성자
        if (this.getId().equals(article.getMemberId())) return true;
        // 슈퍼 관리자
        if (this.isAdmin()) return true;
        // 그외 불가
        return false;
    }

    /* 게시물 삭제 가능 */
    public boolean canDelete(Article article) {
        return canModify(article);
    }
}