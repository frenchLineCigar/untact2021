<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../layout/main_layout_head.jspf" %>

<c:set var="fileInputMaxCount" value="10" />
<script>
ArticleAdd__fileInputMaxCount = ${fileInputMaxCount};
</script>

<script>
let ArticleAdd__isSubmitted = false; // 폼 전송 상태 (중복 전송 방지)

function ArticleAdd__checkAndSubmit(form) {

    if (ArticleAdd__isSubmitted) {
        alert('처리 중입니다.'); // 이미 전송했으면
        return;
    }

    // 제목 유효성 체크
    form.title.value = form.title.value.trim();

    if (form.title.value.length == 0) {
        alert('제목을 입력해주세요.');
        form.title.focus();

        return false;
    }

    // 내용 유효성 체크
    form.body.value = form.body.value.trim();

    if (form.body.value.length == 0) {
        alert('내용을 입력해주세요.');
        form.body.focus();

        return false;
    }

    // 업로드 파일 사이즈 제한
    let maxSizeMb = 10;
    let maxSize = maxSizeMb * 1024 * 1024; //10MB

    for (let inputNo = 1; inputNo <= ArticleAdd__fileInputMaxCount; inputNo++) {
        const input = form["file__article__0__common__attachment__" + inputNo];

        if (input.value) {
            if (input.files[0].size > maxSize) {
                alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
                input.focus();

                return;
            }
        }
    }

    // 파일 전송 함수
    const startUploadFiles = function (onSuccess, onFailure) {

        // Ajax 업로드 요청이 필요한지를 체크하기 위한 변수
        let needToUpload = false;

        for (let inputNo = 1; inputNo <= ArticleAdd__fileInputMaxCount; inputNo++) {
            const input = form["file__article__0__common__attachment__" + inputNo];

            if (input.value.length > 0) { // 파일이 있으면 (페이크 경로 문자열 길이, 파일 없으면 0)
                needToUpload = true; // 업로드 할 첨부파일 있음
                break; // 반복문 빠져나감
            }
        }

        // 첨부파일이 딱히 없다?
        if (needToUpload == false) {
            onSuccess(); // 바로 콜백 실행 -> startSubmitForm
            return;
        }

        // 첨부파일이 있다? Ajax 로 파일 업로드 먼저 ㄱㄱ
        // Ajax로 폼 전송을 하기 위한 FormData 객체 생성
        // Ajax로 폼 전송 시, 기본 폼 동작은 e.preventDefault()로 멈추고, 페이지 전환 없이 폼 데이터를 전송
        const fileUploadFormData = new FormData(form);
        $.ajax({
            url: '/common/file/doUpload',
            data: fileUploadFormData,
            processData: false, // important! -> multipart/form-data
            contentType: false, // important! -> multipart/form-data
            dataType: 'JSON',
            type: 'POST',
            success: onSuccess, // 업로드 성공시 콜백 -> startSubmitForm
            error: onFailure // 실패시 메시지 -> printFallBackMsg
        });
    };

    // 업로드 실패 시
    const printFallBackMsg = function (jqXHR, textStatus, errorThrown) {
        alert(jqXHR.responseText);
        console.log(jqXHR);
        console.log(jqXHR.responseText);
        console.log(jqXHR.status);

        ArticleAdd__isSubmitted = false;
    };

    // 폼 전송 함수
    const startSubmitForm = function (data) {
        if (data && data.body && data.body.fileIdsStr) {
            form.fileIdsStr.value = data.body.fileIdsStr;
        }

        for (let inputNo = 1; inputNo <= ArticleAdd__fileInputMaxCount; inputNo++) {
            const input = form["file__article__0__common__attachment__" + inputNo];
            input.value = ''; // 파일을 이미 startUploadFiles에서 Ajax로 업로드 했으므로, 폼 요소에서 file 타입은 비움
        }

        form.submit();
    };

    ArticleAdd__isSubmitted = true;

    startUploadFiles(startSubmitForm, printFallBackMsg);
}
</script>

<section class="section-1">
  <div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
    <form onsubmit="ArticleAdd__checkAndSubmit(this); return false;" action="doAdd" method="post" enctype="multipart/form-data">
      <input type="hidden" name="fileIdsStr" value=""/>
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
      <c:forEach begin="1" end="${fileInputMaxCount}" var="inputNo">
        <div class="form-row flex flex-col lg:flex-row">
          <div class="lg:flex lg:items-center lg:w-28">
            <span>첨부파일 ${inputNo}</span>
          </div>
          <div class="lg:flex-grow input-file-wrap">
            <input onchange="$(this).next('input[type=button]').show();" type="file" name="file__article__0__common__attachment__${inputNo}" class="form-row-input rounded-sm"/>
            <input onclick="if(confirm('파일 첨부를 취소하시겠습니까?')) $(this).closest('div.input-file-wrap').find('> input[type=file]').val(''); $(this).hide();" type="button" value="취소" class="p-2 rounded-sm" style="display: none">
          </div>
        </div>
      </c:forEach>
      <div class="form-row flex flex-col lg:flex-row">
        <div class="lg:flex lg:items-center lg:w-28">
          <span>작성</span>
        </div>
        <div class="lg:flex-grow">
          <div class="btn-submit">
            <input type="submit" class="btn-primary bg-blue-500 hover:bg-blue-900 text-white font-bold py-2 px-4 rounded" value="완료"/>
            <input onclick="history.back()" type="button" class="btn-info bg-red-500 hover:bg-red-dark text-white font-bold py-2 px-4 rounded" value="취소"/>
          </div>
        </div>
      </div>
    </form>
  </div>
</section>

<%@ include file="../layout/main_layout_foot.jspf" %>