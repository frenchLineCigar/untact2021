<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../layout/main_layout_head.jspf" %>

<script>
    ArticleAdd__isSubmitted = false; // 폼 전송 상태 (중복 전송 방지)
    function ArticleAdd__checkAndSubmit(form) {

        if (ArticleAdd__isSubmitted) {
            alert('처리 중입니다.'); // 이미 전송했으면
            return;
        }
        form.title.value = form.title.value.trim();

        if (form.title.value.length == 0) {
            alert('제목을 입력해주세요.');
            form.title.focus();

            return false;
        }

        form.body.value = form.body.value.trim();

        if (form.body.value.length == 0) {
            alert('내용을 입력해주세요.');
            form.body.focus();

            return false;
        }

        // 업로드 시 파일 사이즈 제한
        var maxSizeMb = 10;
        var maxSize = maxSizeMb * 1024 * 1024; //10MB

        if (form.file__article__0__common__attachment__1.value) {
            if (form.file__article__0__common__attachment__1.files[0].size > maxSize) {
                alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
                form.file__article__0__common__attachment__1.focus();

                return;
            }
        }

        if (form.file__article__0__common__attachment__2.value) {
            if (form.file__article__0__common__attachment__2.files[0].size > maxSize) {
                alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
                form.file__article__0__common__attachment__2.focus();

                return;
            }
        }

        // 폼 전송
        form.submit();
        ArticleAdd__isSubmitted = true;
}
</script>

<section class="section-1">
  <div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
    <form onsubmit="ArticleAdd__checkAndSubmit(this); return false;" action="doAdd" method="post" enctype="multipart/form-data">
      <input type="hidden" name="boardId" value="${param.boardId}"/>
      <div class="form-row flex flex-col lg:flex-row">
        <div class="lg:flex lg:items-center lg:w-28">
          <span>제목</span>
        </div>
        <div class="lg:flex-grow">
          <input type="text" name="title" autofocus="autofocus" class="form-row-input w-full rounded-sm" placeholder="제목을 입력해주세요."/>
        </div>
      </div>
      <div class="form-row flex flex-col lg:flex-row">
        <div class="lg:flex lg:items-center lg:w-28">
          <span>내용</span>
        </div>
        <div class="lg:flex-grow">
          <textarea name="body" class="form-row-input w-full rounded-sm" placeholder="내용을 입력해주세요."></textarea>
        </div>
      </div>
      <div class="form-row flex flex-col lg:flex-row">
        <div class="lg:flex lg:items-center lg:w-28">
          <span>첨부파일 1</span>
        </div>
        <div class="lg:flex-grow">
          <input type="file" name="file__article__0__common__attachment__1" class="form-row-input w-full rounded-sm" />
        </div>
      </div>
      <div class="form-row flex flex-col lg:flex-row">
        <div class="lg:flex lg:items-center lg:w-28">
          <span>첨부파일 2</span>
        </div>
        <div class="lg:flex-grow">
          <input type="file" name="file__article__0__common__attachment__2" class="form-row-input w-full rounded-sm" />
        </div>
      </div>
      <div class="form-row flex flex-col lg:flex-row">
        <div class="lg:flex lg:items-center lg:w-28">
          <span>작성</span>
        </div>
        <div class="lg:flex-grow">
          <div class="btn-submit">
            <input type="submit" class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded" value="완료"/>
            <input onclick="history.back()" type="button" class="btn-info bg-red-500 hover:bg-red-dark text-white font-bold py-2 px-4 rounded" value="취소"/>
          </div>
        </div>
      </div>
    </form>
  </div>
</section>

<%@ include file="../layout/main_layout_foot.jspf" %>