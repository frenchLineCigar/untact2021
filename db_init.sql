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
ALTER TABLE `member` ADD UNIQUE INDEX (`loginId`);

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

# 댓글 리스팅 - 범용 댓글 처리
#1 댓글 테이블 구조 수정 : 댓글은 꼭 특정 글에만 종속되는 것이 아니라 어디에도 붙을 수 있어야 한다 (페북 스타일)
#2 특정 글에 관련된 댓글 검색 시, where 절의 relTypeCode 와 relId와 관련된 고속 검색을 위한 INDEX 생성 (일반 KEY로 인덱스 생성)

# 게시물 전용 댓글에서 범용 댓글로 바꾸기 위해 relTypeCode 추가
ALTER TABLE reply ADD COLUMN `relTypeCode` CHAR(20) NOT NULL AFTER updateDate;

# 현재는 게시물 댓글 밖에 없기 때문에 모든 행의 relTypeCode 칼럼의 값을 article 로 지정
UPDATE reply
SET relTypeCode = 'article'
WHERE relTypeCode = '';

# articleId 칼럼명을 relId로 수정
ALTER TABLE reply CHANGE `articleId` `relId` INT(10) UNSIGNED NOT NULL;

# 고속 검색을 위한 인덱스 생성
ALTER TABLE reply ADD KEY (relTypeCode, relId);
# SELECT * FROM reply WHERE relTypeCode = 'article' AND relId = 5; # 적용 O
# SELECT * FROM reply WHERE relTypeCode = 'article'; # 적용 O
# SELECT * FROM reply WHERE relId = 5 AND relTypeCode = 'article'; # 적용 X
# MySQL 최신 엔진에서는 쿼리 옵티마이저가 인덱스가 적용되지 않는 순서로 적어놔도 검색이 많이 답답하면 알아서 바꿔치기 해서 검색해주겠지만
# WHERE 절 이하의 칼럼 순서에 유의

SELECT R.*,
       IFNULL(M.nickname, "탈퇴회원") AS extra__writer
FROM reply AS R
         LEFT JOIN `member` AS M
                   ON R.memberId = M.id
WHERE 1
  AND R.relTypeCode = 'article'
  AND R.relId = 2
ORDER BY id DESC;

### 외부 로그인을 위해 Member 테이블에 authKey 칼럼 추가 ###

# authKey 칼럼을 추가(UK) : 외부 로그인 처리를 인증할 API Key
ALTER TABLE `member` ADD COLUMN authKey CHAR(80) NOT NULL AFTER loginPw;

# 기존 회원의 authKey 데이터 채우기
UPDATE `member`
SET authKey = CONCAT("authKey1__", UUID(), "__", RAND());

# authKey 칼럼에 유니크 인덱스 추가
ALTER TABLE `member` ADD UNIQUE INDEX (`authKey`);

# 테스트 편의를 위해 예제 데이터의 authKey 고정
# 실제 형태 : authKey1__65ee28a4-988b-11eb-b1f0-705dccf1a82d__0.12214008264442913
UPDATE `member`
SET authKey = 'authKey1__1'
WHERE id = 1;
UPDATE `member`
SET authKey = 'authKey1__2'
WHERE id = 2;

# 파일 테이블 추가
CREATE TABLE `files` (
    id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT, # 번호
    regDate DATETIME DEFAULT NULL, # 작성일
    updateDate DATETIME DEFAULT NULL, # 갱신일
    delDate DATETIME DEFAULT NULL, # 삭제일
    delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 삭제상태(0:미삭제,1:삭제)
    relTypeCode CHAR(50) NOT NULL, # 관련 데이터 타입(article, member)
    relId INT(10) UNSIGNED NOT NULL, # 관련 데이터 번호
    originFileName VARCHAR(100) NOT NULL, # 업로드 당시의 파일명
    fileExt CHAR(10) NOT NULL, # 확장자
    typeCode CHAR(20) NOT NULL, # 종류코드 (common)
    type2Code CHAR(20) NOT NULL, # 종류2코드 (attachment)
    fileSize INT(10) UNSIGNED NOT NULL, # 파일 사이즈
    fileExtTypeCode CHAR(10) NOT NULL, # 파일규격코드(img, video)
    fileExtType2Code CHAR(10) NOT NULL, # 파일규격2코드(jpg, mp4)
    fileNo SMALLINT(2) UNSIGNED NOT NULL, # 파일번호 (1) - 다중파일 업로드시 몇번째 파일인지
    fileDir CHAR(20) NOT NULL, # 파일이 저장되는 폴더명
    PRIMARY KEY (id),
    KEY relId (relId,relTypeCode,typeCode,type2Code,fileNo) # 인덱스 지정해 풀스캔 방지
);

# EX) 게시물(article) 1번의 첫번째 첨부파일만 가져오기
# SELECT * FROM `files`
# WHERE relTypeCode = 'article'
#   AND relId = 1
#   AND typeCode = 'common'
#   AND type2Code = 'attachment'
#   AND fileNo = 1;