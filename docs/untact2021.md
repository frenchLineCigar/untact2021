# Dev Diary 

for my good memory

---

[2021-04-22]

### PLAN
- 글작성
  - 글작성 완료 버튼 클릭 -> 첨부파일 먼저 ajax 로 업로드 -> 업로드 후 응답으로 파일 id 가져오면 -> 글쓰기 폼 전송
  - 1\) 완료 버튼 클릭 시, 첨부파일 먼저 특정위치에 ajax로 업로드
  - 2\) 업로드 응답 결과가 오면 파일저장 후 생성된 파일 id를 작성 폼에 저장 후
  - 3\) 폼 전송
  - 4\) 백단에서는 업로드된 파일을 폼에서 전송된 파일 id로 관계만 맺어줌
  - 장점: 완료 버튼을 눌렀을 때, 파일을 먼저 올려보고서 용량 등이 크면 알맞는 메시지 처리 응답, 추후 뷰를 SPA로 구현하기 위한 초벌작업 
- 최종적으로는 SPA (Single Page Application) 구현
  - 일단 ajax 로 처리하고 -> 추후 axios 로 변경(+ 디자인) 

### TIL
- JS
  - [HTML Input 태그에 포커스(Focus)가 가도록 이벤트 처리하기 : 네이버 블로그](https://m.blog.naver.com/ndb796/221406934376)
  - [[자바스크립트] javascript로 파일 용량 체크하기](https://zzznara2.tistory.com/617)
  - [[ Javascript ] 자바스크립트 상에서 Byte를 보기 쉽게 자동 변환하기](https://aorica.tistory.com/153)
  - [[자바스크립트] Type Checking](https://poiemaweb.com/js-type-check)
  - [[JavaScript] 자바스크립트의 숫자 타입](https://d2fault.github.io/2018/02/28/20180228-javascript-number-type/)
  - [Javascript에서 String을 Number타입으로 바꾸기 :: Outsider's Dev Story](https://blog.outsider.ne.kr/361)
  - [JavaScript :: return vs return true vs return false 차이 :: vida valiente](https://diaryofgreen.tistory.com/80)
  - [return , return true, return false 차이](https://pjd1007.tistory.com/60)

- CSS
  - [[CSS] 스프라이트 이미지](https://velog.io/@tenacity/CSS-%EC%8A%A4%ED%94%84%EB%9D%BC%EC%9D%B4%ED%8A%B8-%EC%9D%B4%EB%AF%B8%EC%A7%80)
  
- SQL
  - [[MySQL] 테이블에서 특정 날짜 이전 데이터 삭제](https://velog.io/@tenacity/MySQL-%ED%85%8C%EC%9D%B4%EB%B8%94%EC%97%90%EC%84%9C-%ED%8A%B9%EC%A0%95-%EB%82%A0%EC%A7%9C-%EC%9D%B4%EC%A0%84-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%82%AD%EC%A0%9C)
  - [[MySQL] 시/분/초 다루기 - 시간/날짜 관련 처리 모음 / datetime, date, timestamp / Java8 LocalDateTime format 변경](https://velog.io/@tenacity/MySQL-%EC%8B%9C%EB%B6%84%EC%B4%88-%EB%8B%A4%EB%A3%A8%EA%B8%B0-%EC%8B%9C%EA%B0%84%EB%82%A0%EC%A7%9C-%EA%B4%80%EB%A0%A8-%EC%B2%98%EB%A6%AC-%EB%AA%A8%EC%9D%8C-datetime-date-timestamp-Java8-LocalDateTime-format-%EB%B3%80%EA%B2%BD)
  - [[SQL] LIKE 연산자 / 기호 연산자(wild card) %, _ 활용](https://thebook.io/006977/ch03/02/02/03/)

- MyBatis
  - [[MyBatis] JOIN / Nested Result 조회 결과 매핑 총정리 - resultMap](https://velog.io/@tenacity/MyBatis-%EC%A1%B0%ED%9A%8C-%EA%B2%B0%EA%B3%BC-%EB%A7%A4%ED%95%91-%EC%B4%9D%EC%A0%95%EB%A6%AC-resultMap)
    
### TODO
 - SPA
 - ajax vs axios vs fetch


> 실패 한 사람들이 현명하게 포기 할 때, 성공한 사람들은 미련하게 참는다.
> Start simple, and evolve it a step at a time!

---