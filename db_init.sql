# 데이터베이스 생성
DROP DATABASE IF EXISTS untact2021;
CREATE DATABASE untact2021;
USE untact2021;

# 게시물 테이블 생성
CREATE TABLE article (
     id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
     regDate DATETIME NOT NULL,
     updateDate DATETIME NOT NULL,
     title CHAR(100) NOT NULL,
     `body` TEXT NOT NULL
);

# 게시물, 테스트 데이터 생성
INSERT INTO article
SET
    regDate = NOW(),
    updateDate = NOW(),
    title = "제목1 입니다.",
    `body` = "내용1 입니다.";

INSERT INTO article
SET
    regDate = NOW(),
    updateDate = NOW(),
    title = "제목2 입니다.",
    `body` = "내용2 입니다.";

INSERT INTO article
SET
    regDate = NOW(),
    updateDate = NOW(),
    title = "제목3 입니다.",
    `body` = "내용3 입니다.";

# 회원 테이블 생성
CREATE TABLE `member` (
      id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
      regDate DATETIME NOT NULL,
      updateDate DATETIME NOT NULL,
      loginId CHAR(30) NOT NULL, #아이디는 30자로 제한 : 한글로 최대 10자까지 가능
      loginPw VARCHAR(100) NOT NULL, #비밀번호 100자로 제한 : 암호화 되어 들어가고, 복호화 할 수 없게 설정, 조회 할 이유 없으므로 VARCHAR
      `name` CHAR(30) NOT NULL, #이름
      nickname CHAR(30) NOT NULL, #닉네임
      email CHAR(100) NOT NULL, #이메일
      cellphoneNo CHAR(20) NOT NULL #전화번호는 20자로 제한
);

# loginId 칼럼 INDEX 설정(UK)
ALTER TABLE `untact2021`.`member` ADD UNIQUE INDEX (`loginId`);

# 회원, 테스트 데이터 생성, user1
INSERT INTO `member`
SET
    regDate = NOW(),
    updateDate = NOW(),
    loginId = "user1",
    loginPw = "user1",
    `name` = "홍길동",
    nickname = "풍운아",
    email = "user1@email.com",
    cellphoneNo = "01012341234";

# 게시물 테이블에 회원번호 칼럼 추가
ALTER TABLE article ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER updateDate;

# 기존 게시물의 작성자를 회원1로 지정 : 억지로 하나 고정해서 맞춰줌
UPDATE article
SET
    memberId = 1
WHERE
    memberId = 0;

# 회원, 테스트 데이터 생성, user2
INSERT INTO `member`
SET
    regDate = NOW(),
    updateDate = NOW(),
    loginId = "user2",
    loginPw = "user2",
    `name` = "강백호",
    nickname = "포기를모르는사나이",
    email = "user2@email.com",
    cellphoneNo = "01011112222";

# user2 작성 게시물 조회
SELECT * FROM article WHERE memberId = 2;

# memberId 값을 바꿔 탈퇴회원(정보가 없는 회원)이라 가정
SELECT * FROM `article`
WHERE id = 2;

# LEFT JOIN (LEFT OUTER JOIN)
SELECT *
FROM article AS A
         LEFT JOIN `member` AS M
                   ON A.memberId = M.id
WHERE A.id = 2;

# INNER JOIN
SELECT *
FROM article AS A
         RIGHT JOIN `member` AS M
                    ON A.memberId = M.id
WHERE A.id = 2;

# RIGHT JOIN (RIGHT OUTER JOIN)
SELECT *
FROM `member` AS M
         RIGHT JOIN article AS A
                    ON A.memberId = M.id
WHERE A.id = 2;

# 원하는 값만 조회, NULL 일 경우 탈퇴회원으로 결과 바인딩
SELECT A.*,
       IFNULL(M.nickname, "탈퇴회원") AS extra__writer
FROM article AS A
         LEFT JOIN `member` AS M
                   ON A.memberId = M.id
WHERE A.id = 2;

# 예제 게시물 추가
/*
INSERT INTO article
(regDate, updateDate, memberId, title, `body`)
SELECT NOW(), NOW(), FLOOR(RAND() * 2) + 1, CONCAT('제목_', FLOOR(RAND() * 1000) + 1), CONCAT('내용_', FLOOR(RAND() * 1000) + 1)
FROM article;
*/

SELECT COUNT(*) FROM article;


# 게시판 테이블 추가
CREATE TABLE board (
   id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
   regDate DATETIME NOT NULL,
   updateDate DATETIME NOT NULL,
   `code` CHAR(20) UNIQUE NOT NULL,
   `name` CHAR(20) UNIQUE NOT NULL
);

# 공지사항 게시판 추가
INSERT INTO board
SET regDate = NOW(),
    updateDate = NOW(),
    `code` = 'notice',
    `name` = '공지사항';

# 자유 게시판 추가
INSERT INTO board
SET regDate = NOW(),
    updateDate = NOW(),
    `code` = 'free',
    `name` = '자유';

SELECT * FROM board;

# 게시물 테이블에 게시판 번호 칼럼 추가, updateDate 칼럼 뒤에 배치
ALTER TABLE article ADD COLUMN boardId INT(10) UNSIGNED NOT NULL AFTER updateDate;

SELECT * FROM article;

# 기존 데이터는 랜덤하게 게시판 번호 지정 (boardId = 1 또는 2)
UPDATE article
SET boardId = FLOOR(RAND() * 2) + 1
WHERE boardId = 0;

SELECT COUNT(*) FROM article WHERE boardId = 1;
SELECT COUNT(*) FROM article WHERE boardId = 2;


# 댓글 테이블 추가
CREATE TABLE reply (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    `body` TEXT NOT NULL
);

INSERT INTO reply
SET regDate = NOW(),
    updateDate = NOW(),
    articleId = 1,
    memberId = 1,
    `body` = "내용1 입니다.";

INSERT INTO reply
SET regDate = NOW(),
    updateDate = NOW(),
    articleId = 1,
    memberId = 2,
    `body` = "내용2 입니다.";

INSERT INTO reply
SET regDate = NOW(),
    updateDate = NOW(),
    articleId = 2,
    memberId = 2,
    `body` = "내용3 입니다.";

SELECT * FROM reply;