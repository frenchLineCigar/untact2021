# Dev Diary 

for my good memory

---

[2021-05-29]

### TIL

<br>

[JAVA] 자바 정규식 패턴
- regex
- **[[Java] 자바 정규 표현식 (Pattern, Matcher) 사용법 & 예제](https://coding-factory.tistory.com/529)**
- **[정규식 포기자를 위한 가장 쉬운 정규식 — 상편](https://to2.kr/cBh)**
- **[정규식 포기자를 위한 가장 쉬운 정규식 — 하](https://to2.kr/cBg)**
- [막상 쓰려면 헷갈리는 정규표현식 모음 - Mk’s Blog](https://moons08.github.io/programming/Regex/)
- [자바 정규식(Regular Expression) 사용하기](https://offbyone.tistory.com/400)
- java only numbers regex
- [너머 :: JAVA 정규표현식 (이메일, 숫자, 아이디)](https://darkhorizon.tistory.com/259)
- [How to check if a string contains only digits in Java - Stack Overflow](https://stackoverflow.com/questions/15111420/how-to-check-if-a-string-contains-only-digits-in-java/15111450)
- [[Java] 문자열이 숫자인지 판별 (isDigit()) :: pridiot](https://pridiot.tistory.com/34)
- [자바(Java) 숫자, 영문, 한글 여부를 체크하는 방법](https://needjarvis.tistory.com/227)

---

[2021-05-28]

### TIL

<br>

[HTTP/WEB]
- referrerpolicy="no-referrer"
- [Referer-Policy이란?](https://scshim.tistory.com/entry/Referer-Policy%EC%9D%B4%EB%9E%80)


---

[2021-05-11]

### TIL

 - OpenAPI 3.0 사용할 경우 : 13. OpenAPI 3.0를 이용한 REST API 문서 만들기만 다뤄봄 (쩌는 링크 있어서 관련 시리즈 복붙)
   - OAS(OpenAPI Specification)는 RESTful 웹서비스를 약속된 규칙에 따라 약속된 규칙에 맞게 API 스펙을 json과 yaml 형식으로 표현
   - Spring Boot에서는 springdoc-openapi 라이브러리를 추가하는 것으로 OAS 3를 사용할 수 있는데, 생성한 api에 별도의 설정을 추가할 경우 웹 애플리케이션의 시작과 동시에 자동으로 문서화 한다.
   - springfox 라이브러리는 OpenAPI 3.0을 지원하고 있지 않습니다.
   - [[Spring Boot Tutorial] 17. Bearer JWT를 이용한 api 인증 (Swagger v3)](https://blog.jiniworld.me/113?category=850715)
   - [[Spring Boot Tutorial] 16. Swagger v3에 HTTP 기본인증(Basic Authentication) 설정하기](https://blog.jiniworld.me/105)
   - [[Spring Boot Tutorial] 15. Open API 3.0 + Swagger v3 상세설정](https://blog.jiniworld.me/91)
   - [[Spring Boot Tutorial] 14. build 환경별 profile 적용하기](https://blog.jiniworld.me/85?category=850715)
   - **[[Spring Boot Tutorial] 13. OpenAPI 3.0를 이용한 REST API 문서 만들기 (Swagger v3)](https://blog.jiniworld.me/83)**
   - [[Spring Boot Tutorial] 12. Spring Boot REST Api에 ResponseEntity 적용하기](https://blog.jiniworld.me/71?category=850715)
   - [[Spring Boot Tutorial] 11. REST API Versioning](https://blog.jiniworld.me/67?category=850715)
   - ['Spring/Spring Boot Tutorial' 카테고리의 글 목록](https://blog.jiniworld.me/category/Spring/Spring%20Boot%20Tutorial?page=1)


 - Springfox / Swagger API Docs
   -  swagger docs path 변경 : InMemorySwaggerResourcesProvider 소스코드 까보면 프로퍼티명 확인 가능 (SwaggerResourcesProvider 을 구현해 Bean으로 등록한 클래스)
     - [springfox.documentation.swagger.v2.path=/whatever/v2/api-docs not working with 3.0.0-SNAPSHOT · Issue #2886 · springfox/springfox · GitHub](https://github.com/springfox/springfox/issues/2886#issuecomment-516543691)
   - 버전 3 이슈 : @ApiModelProperty 의 position 속성은 작동하지 않음
     - 해결 방법 : 없음, 다음 버전 업데이트의 fix를 기다리거나 다운 그레이드 해야 함
     - [Model properties are alphabetically sorted (without order attribute) · Issue #3391 · springfox/springfox · GitHub](https://github.com/springfox/springfox/issues/3391)
     - [springfox ApiModelProperty position sorting not working - Stack Overflow](https://stackoverflow.com/questions/63616811/springfox-apimodelproperty-position-sorting-not-working/63628683#63628683)
     - [@apimodelproperty position not working - Google 검색](https://www.google.com/search?q=%40apimodelproperty+position+not+working&oq=%40apimodelproperty+position+not+working&aqs=chrome..69i57.160j0j7&sourceid=chrome&ie=UTF-8)
   - [Swagger @ApiImplicitParams](https://www.google.com/search?q=%40ApiImplicitParams+%EC%B0%A8%EC%9D%B4%EC%A0%90&oq=%40ApiImplicitParams+%EC%B0%A8%EC%9D%B4%EC%A0%90&aqs=chrome..69i57j0i333.2039j0j7&sourceid=chrome&ie=UTF-8)
   - [Swagger @ApiParam vs @ApiModelProperty](https://www.baeldung.com/swagger-apiparam-vs-apimodelproperty)
   - [Swagger](https://www.baeldung.com/tag/swagger/)
   - [[Spring&Angular] Spring 게시판 만들기 - CRUD 게시판 API](https://walkerlab.tistory.com/10)
   - [[Spring&Angular] Spring 게시판 만들기 - CRUD 게시판 API](https://walkerlab.tistory.com/10)
   - 

---

[2021-05-10]

### TIL

 - HTTP / WEB
   - [에러 status code - Google 검색](https://www.google.com/search?q=%EC%97%90%EB%9F%AC+status+code&sxsrf=ALeKk022_8X_Hwm10rFLObMdb25_3jbSOw%3A1620607072828&ei=YICYYKyMMoiF0wTt1ILABw&oq=%EC%97%90%EB%9F%AC+status+code&gs_lcp=Cgdnd3Mtd2l6EAMyBQgAEM0CMgUIABDNAjIFCAAQzQI6CAgAELADEM0CUO0wWPwzYKs1aANwAHgBgAF1iAGgBZIBAzAuNpgBAKABAaoBB2d3cy13aXrIAQPAAQE&sclient=gws-wiz&ved=0ahUKEwisvJXn773wAhWIwpQKHW2qAHgQ4dUDCA4&uact=5)
   - [HTTP 상태 코드(HTTP status code)](https://velog.io/@honeysuckle/HTTP-%EC%83%81%ED%83%9C-%EC%BD%94%EB%93%9C-HTTP-status-code-#500---599--%EC%84%9C%EB%B2%84-%EC%97%90%EB%9F%AC-%EC%83%81%ED%83%9C-%EC%BD%94%EB%93%9C)
     > 100 - 199 : 정보성 상태 코드  
     200 - 299 : 성공 상태 코드  
     300 - 399 : 리다이렉션 상태 코드  
     400 - 499 : 클라이언트 에러 상태 코드  
     500 - 599 : 서버 에러 상태 코드  
     600이상~
   - API / Open API / Rest API
 
   
 - [Springfox / Swagger 로 API 문서 자동화](https://velog.io/@tenacity/SpringRestAPI-Swagger%EB%A1%9C-API-%EB%AC%B8%EC%84%9C-%EC%9E%90%EB%8F%99%ED%99%94)
   - Swagger는 Web API 문서화를 위한 도구이다
     - API Documentation & Design Tools, OAS(Open API Specification)
   - [GitHub - springfox/springfox: Automated JSON API documentation for API's built with Spring](https://github.com/springfox/springfox#springfox)
   - Swagger 개요 및 프로젝트 적용
     - Swagger 3 / Springfox Boot Starter 프로젝트 적용 및 3.0.0+ 최신버전 유의사항
       - swagger 3.0 오면서 편해졌다. springfox-boot-starter 하나만 추가 하면 얘가 필요한 dependency를 다 들고 있기 때문에 신경 쓸게 적다.
       - 주의할 점은 swagger 2.0과 달리 기본 swagger-ui 접속 URL이 http://localhost:8080/swagger-ui.html이 아닌 http://localhost:8080/swagger-ui/로 바뀌었다는 점이다.
       - 이 점을 몰라서 “왜 작동이 안되지? “하면서 한참 헤맸다. 스프링 설정 구성 시 꼭 알아둬야 할 사항이다.
       - [Setting up Swagger 3 With Spring Boot](https://medium.com/@hala3k/setting-up-swagger-3-with-spring-boot-2-a7c1c3151545)
       - [Springfox swagger-ui.html not loading using version 3.0.0-SNAPSHOT · Issue #3362 · springfox/springfox · GitHub](https://github.com/springfox/springfox/issues/3362)
       - [java-swagger-ui-3.0-사용하기 - Deliwind](https://blog2.deliwind.com/20201127/java-swagger-ui-3-0-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0/)
     - 기타 참고 사항
       - [Springfox Reference Documentation](https://springfox.github.io/springfox/docs/current/)
       - [API 문서 자동화 - Swagger](https://velog.io/@tigger/API-%EB%AC%B8%EC%84%9C-%EC%9E%90%EB%8F%99%ED%99%94-Swagger)
       - [OpenAPI 3.0을 사용할 경우 / Spring REST API 문서화](https://recordsoflife.tistory.com/342)
       - [안경잡이개발자 :: Swagger Hub를 이용한 REST API 관리](https://ndb796.tistory.com/249)
       - [Spring Boot에 Swagger 적용하기](https://hyeran-story.tistory.com/73)
       - [SwaggerHub를 사용하여 API 관리하기 :: 전자기린 스튜디오](https://jsmun.com/54)
       - [Swagger 개요 및 설정](https://velog.io/@oyeon/Swagger-%EC%84%A4%EC%A0%95)
       - [Spring Swagger 적용](https://augustines.tistory.com/206)
       - [Swagger 사용하기(Swagger-ui 적용) : 네이버 블로그](https://m.blog.naver.com/PostView.nhn?blogId=jjeaby&logNo=220968368638&proxyReferer=https:%2F%2Fwww.google.com%2F)
   
 
 - JS / jQuery / Ajax
   - [jQuery Form onsubmit 이벤트 시 ajax 결과에 따라서 전송 처리하기 - async: false :: 사이트 구축/최적화 활용 팁](https://han288.tistory.com/69)



---

[2021-05-09]

### TIL

 - CSS/Tailwind
   - tailwind chip
     - [tailwind chips](https://codepen.io/junchow/pen/GRoJzqN)
     - [Chip UI Kit - Tailwind CSS Design](https://tailwindesign.com/components/chip)
     - [Chips by haynajjar](https://tailwindcomponents.com/component/chips)
     - [Svelte and tailwindcss Chip component - DEV Community](https://dev.to/haynajjar/svelte-and-tailwindcss-chip-component-4oh0)
     - [Smelte: Material design using Tailwind CSS for Svelte](https://smeltejs.com/components/chips/)
   - [Font Weight - Tailwind CSS](https://tailwindcss.com/docs/font-weight)
   - [Font Size - Tailwind CSS](https://tailwindcss.com/docs/font-size)
   - [css tip 5. margin과 padding의 차이점 (마진과 패딩의 차이점) : 블루씰 디자인](https://m.blog.naver.com/codbs7/221421798264)
   - [Display - Tailwind CSS](https://tailwindcss.com/docs/display#hidden)
   - tailwind responsive/breakpoint : xs 없음
     - [Breakpoints - Tailwind CSS](https://tailwindcss.com/docs/breakpoints)
     - [Responsive Design - Tailwind CSS](https://tailwindcss.com/docs/responsive-design)
     - [Container - Tailwind CSS](https://tailwindcss.com/docs/container)
   - tailwind cursor pointer
     - [Cursor - Tailwind CSS](https://tailwindcss.com/docs/cursor)
     - [Pointer Events - Tailwind CSS](https://tailwindcss.com/docs/pointer-events)


 - CSS/FontAwesome
   - [Detail Icons](https://fontawesome.com/icons?d=gallery&p=2&q=detail&m=free)
   - [Edit Icons](https://fontawesome.com/icons?d=gallery&p=2&q=edit&m=free)
   - [Delete Icons](https://fontawesome.com/icons?d=gallery&p=2&q=delete&m=free)


 - MyBatis
   - [MyBatis, Dynamic SQL 타입별 기본값](https://jsonobject.tistory.com/240)
   - [[Mybatis] 동적쿼리 (if test) 문자열처리](https://developyo.tistory.com/242)
   - [[MyBatis] 동적 쿼리 if문 문법 총 정리](https://java119.tistory.com/42)
   - [mybatis dynamic empty string check - Google 검색](https://www.google.com/search?q=mybatis+dynamic+empty+string+check&sxsrf=ALeKk00EdALPDNAj1E5QcFHtb-eeZWsRMg%3A1620563251855&ei=M9WXYObZM6uWr7wP3oKt-AM&oq=mybatis+dynamic+empty+string+check&gs_lcp=Cgdnd3Mtd2l6EAMyBwghEAoQoAEyBwghEAoQoAEyBAghEBU6BwgjELADECc6BQgAEM0COgUIIRCgAVDKLFjQMmC5M2gEcAB4AIABmQGIAYUGkgEDMC42mAEAoAEBqgEHZ3dzLXdpesgBAcABAQ&sclient=gws-wiz&ved=0ahUKEwimv9nHzLzwAhUry4sBHV5BCz8Q4dUDCA4&uact=5)
     - [How to check for an empty string in MyBatis? - Stack Overflow](https://stackoverflow.com/questions/15436712/how-to-check-for-an-empty-string-in-mybatis)
     - [MyBatis에서 null과 nullString을 체크할 때](https://showbang.github.io/typistShow/2017/04/11/nullCheck/)
     - [MyBatis isNull isEmpty 사용하기](https://cofs.tistory.com/309)
     - [스토브 훌로구 :: mybatis 동적쿼리 조건문에 isEmpty, isNotEmpty 를 써보자.](https://stove99.tistory.com/73)
     - [OKKY - myBatis empty()함수 사용하기](https://okky.kr/article/179882)


 - JS
   - [javascript empty string check - Google 검색](https://www.google.com/search?q=javascript+empty+string+check&oq=javascript+empty+string&aqs=chrome.1.69i57j0l3.4447j0j7&sourceid=chrome&ie=UTF-8)
     - [How to Check for an Empty String in JavaScript](https://www.tutorialrepublic.com/faq/how-to-check-for-an-empty-string-in-javascript.php)
     - [JavaScript에서 빈 문자열을 확인하는 방법](https://www.delftstack.com/ko/howto/javascript/javascript-check-if-string-is-empty/)
     - [소금인형 - SW개발자? :: Javascript의 null 과 empty string등 비교](https://blog.edit.kr/entry/Javascript%EC%9D%98-null-%EA%B3%BC-empty-string%EB%93%B1-%EB%B9%84%EA%B5%90)

---


[2021-05-08]

### TIL

 - SQL
   - [[SQL] GROUP_CONCAT 함수로 서로 다른 ROW들의 문자열 집계 (with IFNULL / ISNULL / COALESCE / NULLIF )](https://velog.io/@tenacity/SQL-GROUPCONCAT-%ED%95%A8%EC%88%98%EB%A1%9C-%EC%84%9C%EB%A1%9C-%EB%8B%A4%EB%A5%B8-ROW%EB%93%A4%EC%9D%98-%EB%AC%B8%EC%9E%90%EC%97%B4-%EC%A7%91%EA%B3%84)
   - [Data Story: MySQL INT, BIGINT, TINYINT의 차이 및 INT(10), BIGINT(10), TINYINT(10)의 차이](https://parklize.blogspot.com/2014/09/mysql-int-bigint-tinyint-int10-bigint10.html)
   - [MySQL, 데이터형과 범위 : 네이버 블로그](https://m.blog.naver.com/dudwo567890/220847437396)
 - WEB
   - [<input type="tel"\> - HTML: HyperText Markup Language](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/tel)
   - [<input type="email"\> - HTML: HyperText Markup Language](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/email)
   - [<input\>: 입력 요소 - HTML: Hypertext Markup Language](https://developer.mozilla.org/ko/docs/Web/HTML/Element/input)
   - minlength / maxlength : 문자열 길이 밸리데이션
   - max / min : 최대/최소 값 밸리데이션

---


[2021-05-06]

### TIL

> 
> 오류를 예측해서 비정상적인 상황이 발생하지 않게 하는 것은 정말 중요하다고 생각한다.
> Multipart 처리 시 톰캣의 FileUploadException 하위 타입 예외가 발생하면, 이를 스프링은 MultipartException 으로 간주한다.
> 컨트롤러 진입 전 애플리케이션 레벨의 서블릿 수준에서 발생하는 예외이므로
> 간단히 @ControllerAdvice 레벨에서 @ExceptionHandler 를 구현해 예외를 잡고 커스텀 에러 메시지를 담아 응답으로 보내주면
> 되겠다라고 생각했다. 
> 
> 하지만 문제가 발생했다.
> 하나의 파일에 대해서는 서버에서 Response Body에 담은 커스텀 에러 메시지를 정상적으로 꺼내서 출력할 수 있지만
> 다중 파일 요청의 경우, 각 파일에 대해 각각의 Exception 이 터지고 있었고
> 서버의 익셉션 처리 결과를 응답 바디에 담아보내도 클라이언트에서 정상적으로 꺼낼 수 없었다.
> 결과적으로 Ajax 의 error or fail 콜백에서 Response Body의 항목을 jqXHR.responseText 로 꺼내려고 하면 'undefined' 가 출력된다.
> 서버에서 바디에 내려준 특정 데이터를 정상적으로 수신할 수가 없으므로 클라이언트 단에서 어떤 조건에 맞는 처리가 불가능하다.
> 어떻게 해결하면 좋을까?
>
> 그 이유와 해결 방법을 알아보고 삽질 연대기 기록
>
> 결론: 
>```
># 비동기 요청 Body의 최대 크기에 대한 값을 -1 로 주어, 사용자 정의 예외처리 구현하도록 한다.(핵심 설정)
># Maximum amount of request body to swallow.
>server:
>  tomcat:
>    max-swallow-size: -1
>```
>

 - 간단히 끝날 줄 알았던, 그러나 실패를 거듭하며 더 많은 것을 얻을 수 있었던 SizeLimitExceededException, FileSizeLimitExceededException, MaxUploadSizeExceededException 처리 삽질 기록
   - [[MultipartException] MultipartException 관련 ExceptionHandler 구현 시 주요 설정 사항](https://velog.io/@tenacity/MultipartException-ExceptionHandler-%EA%B5%AC%ED%98%84-%EC%8B%9C-%EC%A3%BC%EC%9A%94-%EC%84%A4%EC%A0%95-%EC%82%AC%ED%95%AD-SizeLimitExceededException-FileSizeLimitExceededException-MultipartException)
     - MaxUploadSizeExceededException, SizeLimitExceededException, FileSizeLimitExceededException
 
 - HTTP Response
   - [[jQuery] Ajax 에러(error) 처리 -jqXHR, readyState 오류 디버그 : 네이버 블로그](https://m.blog.naver.com/afidev/20184722536)
   - [[Spring Boot] 클라이언트 REST API 응답보내기 :: Gyun's 개발일지](https://devlog-wjdrbs96.tistory.com/197?category=882974)  
   - [REST API 제대로 알고 사용하기 : NHN Cloud Meetup](https://meetup.toast.com/posts/92)
   - [[Spring Boot] ResponseEntity란 무엇인가? :: Gyun's 개발일지](https://devlog-wjdrbs96.tistory.com/182)
   - [[Spring Boot] Spring boot로 ResponseEntity 적용하기 — Steemit](https://steemit.com/kr-dev/@igna84/spring-boot-responseentity)
   - [[Spring Boot] 예외처리 @ExceptionHandler 리팩토링 코드 가이드](https://freedeveloper.tistory.com/235)
   - [HTTP Error 413 request entity too large - failure when attaching files](https://confluence.atlassian.com/jirakb/http-error-413-request-entity-too-large-failure-when-attaching-files-693900009.html)
   
 - Java
   - [How to Find an Exception's Root Cause in Java](https://www.baeldung.com/java-exception-root-cause)
   - [Java 에서 Exception 의 내용을 추출하고 싶을 때](https://mainia.tistory.com/723)
---

[2021-05-05]

### TIL

>
> 하.. 왜 이렇게 자스 모르는게 많냐
> 하나부터 열까지 더듬더듬 한다
> 이번 기회에 트레이닝 ㄱㄱ
>

 - JS / jQuery 제이쿼리 졸라 파기
   - [[JavaScript] 호이스팅(Hoisting)과 함수표현식 권장 이유](https://deftkang.tistory.com/17)
     - [함수 선언문 함수 표현식 호이스팅](https://www.google.com/search?q=%ED%95%A8%EC%88%98+%EC%84%A0%EC%96%B8%EB%AC%B8+%ED%95%A8%EC%88%98+%ED%91%9C%ED%98%84%EC%8B%9D+%ED%98%B8%EC%9D%B4%EC%8A%A4%ED%8C%85&sxsrf=ALeKk01UuOqc_bBOMwze9DoE_D3QUzn4hg%3A1620293538028&ei=oreTYNSYAZC5mAXQuaywDA&oq=%ED%95%A8%EC%88%98+%EC%84%A0%EC%96%B8%EB%AC%B8+%ED%95%A8%EC%88%98+%ED%91%9C%ED%98%84%EC%8B%9D+%ED%98%B8%EC%9D%B4%EC%8A%A4%ED%8C%85&gs_lcp=Cgdnd3Mtd2l6EAMyBQghEKABMgUIIRCgATIFCCEQoAEyBQghEKABMgUIIRCgAToHCAAQsAMQHjoECCMQJzoHCCEQChCgAVChCFjkE2DAFGgHcAB4A4ABnQGIAccLkgEEMC4xMZgBAKABAaoBB2d3cy13aXrIAQHAAQE&sclient=gws-wiz&ved=0ahUKEwjU8Y_m37TwAhWQHKYKHdAcC8YQ4dUDCA4&uact=5)
   - [DOM selector API and Array](https://codepen.io/kschoi/pen/vqYgmX)
   - [<input type="file"\> - HTML: Hypertext Markup Language](https://developer.mozilla.org/ko/docs/Web/HTML/Element/Input/file)
   - [check if form file input exist](https://www.google.com/search?q=check+form+file+input+exist&oq=check+form+file+input+exist&aqs=chrome..69i57j33i160.18284j1j9&sourceid=chrome&ie=UTF-8)
     - [How to check input file is empty or not using JavaScript/jQuery ? - GeeksforGeeks](https://www.geeksforgeeks.org/how-to-check-input-file-is-empty-or-not-using-javascript-jquery/)
     - [Check if File Exists Before upload - Stack Overflow](https://stackoverflow.com/questions/47144036/check-if-file-exists-before-upload)
   - jQuery prop callback : .prop( propertyName, function )
     - [.prop()](https://api.jquery.com/prop/#prop-propertyName-function)
   - 제이쿼리 와일드 카드 (jquery wild card selector)
     - [[JavaScript/jQuery] Selector에서 와일드카드 사용하기](https://fascinate-zsoo.tistory.com/16)
   - [제이쿼리 객체. jQuery 객체에 대해 알아보자.](https://violetboralee.medium.com/jquery-object-20094ebac68d)
      - [jQuery와 DOM 첫걸음](https://www.nextree.co.kr/p9747/)
      - [CSS: 선택자(Selector) 이해](https://www.nextree.co.kr/p8468/)
      - [jQuery Node to DOM - Google 검색](https://www.google.com/search?q=jquery+node+to+dom&sxsrf=ALeKk00yMkA1il1XM8LZahDjlSb675gsVw%3A1620291876243&ei=JLGTYIGwDtDT-QaE56LQDw&oq=jquery+node+to+dom&gs_lcp=Cgdnd3Mtd2l6EAMyBggAEAgQHjoFCAAQsAM6CAgAELADEMsBOgcIABCwAxAeOgIIADoFCAAQywE6BwgAEIcCEBQ6BAgjECc6BQgAELEDOgQIABAeUJqYCFjsrAhgxcAIaANwAHgBgAHOAYgB0BGSAQYwLjE2LjGYAQCgAQGqAQdnd3Mtd2l6yAEKwAEB&sclient=gws-wiz&ved=0ahUKEwiB0tzN2bTwAhXQad4KHYSzCPoQ4dUDCA4&uact=5)
      - [Jquery Object to DOM - Google 검색](https://www.google.com/search?q=jquery+object+to+dom&oq=jquery+object+to+dom&aqs=chrome..69i57j0j0i30l2j0i8i30l2j0i5i30l4.14071j0j7&sourceid=chrome&ie=UTF-8)
        - [Working with JavaScript DOM objects vs. jQuery objects](https://howtodoinjava.com/jquery/javascript-dom-objects-vs-jquery-objects/)
        - [jQuery 에서 DOM elements에 직접 접근하는 방법](https://appletree.or.kr/blog/notes/jquery%EC%97%90%EC%84%9C-dom-elements%EC%97%90-%EC%A7%81%EC%A0%91-%EC%A0%91%EA%B7%BC%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95/)
        - [jQquery Object를 HTML DOM Object로 변환하기](https://catcape.tistory.com/entry/jquery-object%EB%A5%BC-html-dom-object%EB%A1%9C-%EB%B3%80%ED%99%98%ED%95%98%EA%B8%B0)
        - [jQuery 자신을 포함한 html값 알아내기 - innerHTML, outerHTML](https://okkks.tistory.com/940)
        - [[jQuery] jQuery 객체에서 Dom 객체 가져오기](https://notpeelbean.tistory.com/entry/jQuery-jQuery-%EA%B0%9D%EC%B2%B4%EC%97%90%EC%84%9C-Dom-%EA%B0%9D%EC%B2%B4-%EA%B0%80%EC%A0%B8%EC%98%A4%EA%B8%B0)
        - [jQuery outerHTML 기능 사용](https://do-study.tistory.com/1)
 - MyBatis / SQL
   - <discriminator\> 조건으로 동적인 결과 맵핑 시, 테이블끼리 PK 칼럼명이 같을 경우 한쪽 테이블 PK 칼럼명에 Alias 걸어 맵핑
     - [PHP, Mysql: SELECT JOIN 문 실행시 중복된 이름의 열(컬럼) 결과를 가져오는 방법 - BGSMM](http://yoonbumtae.com/?p=3277)  
     - [MyBatis - 조회결과 맵핑(ResultMap)](https://blog.daum.net/cjsghkz/79) 
  
---

[2021-04-29]

### TIL

> 스프링은 디폴트로 UnCheckedException 과 Error에 대해서만 롤백 정책을 설정한다.
>
> 그럼 CheckedException 발생 시 트랜잭션 롤백 처리는 어떻게 해야할까? (예: 파일 입출력 예외(IOException) 발생 시)

 - [[Spring] @Transactional 롤백은 언제 되는 걸까? - 예외가 발생했는데도 DB 반영이 된다고?](https://pjh3749.tistory.com/269) 
   - 스프링은 RuntimeException 과 Error 를 기본적인 롤백 정책으로 사용
   - 즉 Transactional의 rollbackFor 속성을 별도로 주지 않는다면, @Transactional(rollbackFor = {RuntimeException.class, Error.class}) 로 이해함
   - 따라서 IOException 처럼 CheckedException 이 발생했을 때는 트랜잭션이 롤백되지 않고, DB는 변경사항이 반영됨
   - 일일히 각각의 Exception 을 따로 명시하는 것은 번거로우므로,
   - 모든 예외에 대해서 전부 트랜잭션을 롤백하기 위해 @Transactional(rollbackFor = Exception.class) 로 처리
 
 - Spring/Java
   - [Returning Image/Media Data with Spring MVC](https://www.baeldung.com/spring-mvc-image-media-data)
   - [Java - Convert File to InputStream](https://www.baeldung.com/convert-file-to-input-stream)
   - [Returning an Image or a File with Spring](https://www.baeldung.com/spring-controller-return-image-file)
   - [MediaType (Spring Framework 5.3.6 API)](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/MediaType.html)
   - [[HTTP & Spring MVC] 파일 다운로드 구현시 파일명 지정](https://namocom.tistory.com/391)
 
 - HTTP : Response Header 'Content-Disposition'
   - [[RFC 6266] Content-Disposition in HTTP](https://namocom.tistory.com/393#footnote_link_393_1)
 
### TODO
 - 글 작성
   - 첨부파일 생성 DONE
 - 글 삭제
   - 첨부파일 제거 DONE
 - 글 수정
   - 첨부파일 생성 DONE
   - 첨부파일 수정 DONE
   - 첨부파일 제거 DONE

---


[2021-04-28]

### TIL

 - Java : Number Formatting
   - [Formatting Large Integers With Commas](https://www.baeldung.com/java-number-formatting#1-formatting-large-integers-with-commas)
   - [자바 숫자 Format 변경 - DecimalFormat](https://all-record.tistory.com/192)
   - [[JAVA] NumberFormat 클래스](https://xzio.tistory.com/329)
   - [[JAVA] DecimalFormat 클래스](https://xzio.tistory.com/330)
---

[2021-04-27]

### TIL

 - SQL : MySQL 정렬 시 우선 순위 차등과 조건에 따른 정렬
   - [[MySQL] ORDER BY 특정 값 우선 정렬 하기 (ORDER BY FIELD) :: 개발 잡동사니 저장소](https://jabstorage.tistory.com/30)
   - [MySQL FIELD() Function](https://www.w3schools.com/sql/func_mysql_field.asp)
   - [MySQL #2_ SELECT 조건에 따른 데이터 검색](https://doorbw.tistory.com/22)
   - [[MySQL] 8장 다양한 조건으로 데이터 추출하기 - 회복맨 블로그](https://recoveryman.tistory.com/172)
   - 
   
 - JSTL/EL
   - [[Servlet/JSP] 표현언어(EL)에서 ${}과 #{} 표기법의 차이](https://velog.io/@tenacity/ServletJSP-%ED%91%9C%ED%98%84%EC%96%B8%EC%96%B4EL%EC%97%90%EC%84%9C-%EA%B3%BC-%ED%91%9C%EA%B8%B0%EB%B2%95%EC%9D%98-%EC%B0%A8%EC%9D%B4)
   - [[JSTL core] [c:forEach] varStatus를 활용한 변수](https://velog.io/@tenacity/JSTL-core-cforEach-varStatus%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%B3%80%EC%88%98)
   - [[링크모음] JSTL 케바케 활용](https://velog.io/@tenacity/%EB%A7%81%ED%81%AC%EB%AA%A8%EC%9D%8C-JSTL-%EB%AC%B8%EB%B2%95)
  
  - JAVA
    - [[Java 8] 변환 추출 쪼개기 합치기 거르기 모으기](https://velog.io/@tenacity/Java-8-%EB%B3%80%ED%99%98-%EC%B6%94%EC%B6%9C-%EC%AA%BC%EA%B0%9C%EA%B8%B0-%ED%95%A9%EC%B9%98%EA%B8%B0-%EA%B1%B0%EB%A5%B4%EA%B8%B0-%EB%AA%A8%EC%9C%BC%EA%B8%B0)
    
---

[2021-04-24]

### PLAN
파일 업로드 -> 글 저장 -> 생선된 글 번호를 업로드 파일의 연관 게시물 번호로 갱신

### TIL

- HTTP
  - [리다이렉션을 처리하는 여러가지 방식](https://sundries-in-myidea.tistory.com/58)
  - [방문경로(referrer/refferer)가 검출되지 않는경우](https://jang8584.tistory.com/46)
  - [Referer - HTTP](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Referer)
  - [Redirections in HTTP - HTTP](https://developer.mozilla.org/ko/docs/Web/HTTP/Redirections)
- Postman file upload test
  - [postman file upload - Google 검색](https://www.google.com/search?q=postman+file+upload&oq=postman+file+upload&aqs=chrome..69i57j0l9.3098j0j9&sourceid=chrome&ie=UTF-8)
  - [Postman에서 파일 전송 처리하기](https://elfinlas.github.io/2019/01/16/postman-insert-img/)
  - [Postman으로 파일전송 테스트 하기](https://velog.io/@k904808/Postman%EC%9C%BC%EB%A1%9C-%ED%8C%8C%EC%9D%BC%EC%A0%84%EC%86%A1-%ED%85%8C%EC%8A%A4%ED%8A%B8-%ED%95%98%EA%B8%B0)
  - [MultipartFile Upload/Download. 현 프로젝트에서 모바일을 위한 멀티파트 업/다운로드를 따로 개발을…](https://medium.com/@kwangsoo/multipartfil-upload-download-a236bb71093e)
  - [chrome network to postman](https://www.google.com/search?q=chrome+network+to+postman&oq=chrome+network+to+postman&aqs=chrome..69i57j0i8i19i30l2.5946j0j7&sourceid=chrome&ie=UTF-8)
  - [How to replicate requests from the Chrome Network Tab into Postman?](https://medium.com/@tiboprea/how-to-replicate-requests-from-the-chrome-network-tab-into-postman-4ec6016ee18c)
  
- JS
  
### TODO

> 천천히 가는 것을 걱정하지 말고 서있는 것을 걱정하라.
>

---

[2021-04-23]

### PLAN

### TIL

- JSP/JSTL
  - [JSTL fn:length() Function - javatpoint](https://www.javatpoint.com/jstl-fn-length-function)
  - [JSTL Core c:out Tag - javatpoint](https://www.javatpoint.com/jstl-core-out-tag)
  - [[JSP] JSTL에서 <c:out> 태그를 쓰는 이유](https://velog.io/@tenacity/JSP-JSTL%EC%97%90%EC%84%9C-cout-%ED%83%9C%EA%B7%B8%EB%A5%BC-%EC%93%B0%EB%8A%94-%EC%9D%B4%EC%9C%A0)
  - [jsp 페이지에서 jstl을 사용하여 HTML tag 제거하기](https://offbyone.tistory.com/263)
  - [JSTL - <c:forEach>, <c:forTokens> 태그 사용법](https://offbyone.tistory.com/368)
  
- JS
  - [클로저 - JavaScript](https://developer.mozilla.org/ko/docs/Web/JavaScript/Closures#%ED%81%B4%EB%A1%9C%EC%A0%80closure)
  - [자바스크립트의 IIFE](https://velog.io/@doondoony/javascript-iife)
  - [[JS] 함수선언문과 함수표현식의 차이](https://gmlwjd9405.github.io/2019/04/20/function-declaration-vs-function-expression.html)
  - [[JS] 함수형 Array.fill()](https://velog.io/@tenacity/JS-%ED%95%A8%EC%88%98%ED%98%95-Array.fill)
  
### TODO

>
>

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
  - [[JS/jQuery] AJAX multipart/form-data 파일 전송 - data 파싱, processData, contentType, dataType](https://velog.io/@tenacity/JSjQuery-AJAX-multipartform-data-%ED%8C%8C%EC%9D%BC-%EC%A0%84%EC%86%A1-data-%ED%8C%8C%EC%8B%B1-processData-contentType-dataType)
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