<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.tena.untact2021.util.Util" %>

<%@ include file="../layout/main_layout_head.jspf" %>

<c:set var="fileInputMaxCount" value="10" />
<script>
    ArticleModify__fileInputMaxCount = parseInt("${fileInputMaxCount}");
    const articleId = parseInt("${article.id}");
</script>

<script>
    let ArticleModify__isSubmitted = false; // 폼 전송 상태 (중복 전송 방지)

    function ArticleModify__checkAndSubmit(form) {

        if (ArticleModify__isSubmitted) {
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

        for (let fileNo = 1; fileNo <= ArticleModify__fileInputMaxCount; fileNo++) {
            const input = form["file__article__"+ articleId +"__common__attachment__" + fileNo];

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

						// 업로드할 파일 유무 체크
            for (let fileNo = 1; fileNo <= ArticleModify__fileInputMaxCount; fileNo++) {
                const input = form["file__article__"+ articleId +"__common__attachment__" + fileNo];

								if (input.value.length > 0) { // 파일이 있으면 (페이크 경로 문자열 길이, 파일 없으면 0)
                    needToUpload = true; // 업로드 할 첨부파일 있음
                    break; // 반복문 빠져나감
                }
            }

            // 업로드할 파일이 없다면 삭제할 파일을 마저 체크
            if (needToUpload == false) {
                // 삭제할 파일 체크
                for (let fileNo = 1; fileNo <= ArticleModify__fileInputMaxCount; fileNo++) {
                    const deleteInput = form["deleteFile__article__"+ articleId +"__common__attachment__" + fileNo];

                    if (deleteInput && deleteInput.checked) { // 삭제 체크가 하나라도 있으면
                        needToUpload = true; // 삭제할 첨부파일 있음
                        break; // 반복문 빠져나감
                    }
                }
            }

            // 변경 사항이 딱히 없다?
            if (needToUpload == false) {
                onSuccess(); // 바로 콜백 실행 -> startSubmitForm
                return;
            }

            // 변경 사항이 있다? Ajax 로 변경사항 반영 ㄱㄱ
            const fileUploadFormData = new FormData(form);
            $.ajax({
                url: '/common/file/doUpload',
                data: fileUploadFormData,
                processData: false, // important! -> multipart/form-data
                contentType: false, // important! -> multipart/form-data
                dataType: "json",
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
            // console.log(data);
            // console.log(data.resultCode);
            // console.log(data.msg);
            // console.log(data.body);

            if (data && data.body && data.body.fileIdsStr) {
                form.fileIdsStr.value = data.body.fileIdsStr;
            }

            // 파일 비움
            for (let fileNo = 1; fileNo <= ArticleModify__fileInputMaxCount; fileNo++) {
                // 업로드 파일 비움
                const input = form["file__article__"+ articleId +"__common__attachment__" + fileNo];
                input.value = ''; // 파일을 이미 startUploadFiles에서 Ajax로 업로드 했으므로, 폼 요소에서 file 타입은 비움

		            // 파일 삭제 체크박스 해제
                const deleteInput = form["deleteFile__article__"+ articleId +"__common__attachment__" + fileNo];
                if (deleteInput) {
                    deleteInput.checked = false;
                }
                // console.log(deleteInput); // logs the expandable <html>…</html>
                // console.dir(deleteInput); // logs the element’s properties and values
                // console.log(deleteInput.outerHTML);
                // console.log('deleteInput.value ---> ' + input.value);
                // console.log('deleteInput.checked --->' + input.checked);
            }

            form.submit();
        };

        ArticleModify__isSubmitted = true;

        startUploadFiles(startSubmitForm, printFallBackMsg);
    }
</script>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<form onsubmit="ArticleModify__checkAndSubmit(this); return false;" action="doModify" method="post" enctype="multipart/form-data">
			<input type="hidden" name="fileIdsStr" value=""/>
			<input type="hidden" name="id" value="${article.id}"/>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>제목</span>
				</div>
				<div class="lg:flex-grow">
					<input value="${article.title}" type="text" name="title" autofocus="autofocus" class="form-row-input w-full rounded-sm" placeholder="제목을 입력해주세요."/>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>내용</span>
				</div>
				<div class="lg:flex-grow">
					<textarea name="body" class="form-row-input w-full roaunded-sm" placeholder="내용을 입력해주세요."><c:out value="${article.body}" /></textarea>
				</div>
			</div>

			<c:set var="fileMap" value="${article.extra.fileMap}" />
			<c:forEach var="fileNo" begin="1" end="${fileInputMaxCount}">
				<c:set var="file" value="${fileMap[fileNo]}" />
				<div class="form-row flex flex-col lg:flex-row">
					<div class="lg:flex lg:items-center lg:w-28">
						<span>첨부파일 ${fileNo}</span>
					</div>
					<div class="lg:flex-grow input-file-wrap">
						<input onchange="$(this).next('input[type=button]').show();" type="file" class="add-file-input form-row-input rounded-sm" name="file__article__${article.id}__common__attachment__${fileNo}" />
						<input type="button" class="cancel-file-input p-2 rounded-sm" value="취소" style="display: none">
						<c:if test="${file != null}">
							<div class="existing-file-item">
								<%-- 파일명/용량 표시--%>
								<div>
									<a href="${file.downloadUrl}" target="_blank" class="existing-file text-blue-500 hover:underline">${file.originFileName}</a> (${file.fileSizeWithUnit})
								</div>
								<%-- 파일이 이미지인 경우 --%>
								<c:if test="${file.fileExtTypeCode == 'img'}">
									<div class="img-box img-box-auto">
										<a href="${file.forPrintUrl}" target="_blank" class="inline-block" title="자세히 보기">
											<img class="max-w-sm" src="${file.forPrintUrl}">
										</a>
									</div>
								</c:if>
								<%-- 파일이 비디오인 경우 --%>
								<c:if test="${file.fileExtTypeCode == 'video'}">
									<div class="video-box">
										<video preload="auto" controls src="${file.forPrintGenUrl}"></video>
									</div>
								</c:if>
								<div>
									<label>
										<input onclick="$(this).closest('div.input-file-wrap').find('> input[type=file]').val('');" type="checkbox" name="deleteFile__article__${article.id}__common__attachment__${fileNo}" value="Y" />
										<span>삭제</span>
									</label>
								</div>
							</div>
						</c:if>
					</div>
				</div>
			</c:forEach>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>수정</span>
				</div>
				<div class="lg:flex-grow">
					<div class="btn-submit">
						<input type="submit" class="btn-primary bg-blue-500 hover:bg-blue-900 text-white font-bold py-2 px-4 rounded" value="수정"/>
						<input onclick="history.back()" type="button" class="btn-info bg-red-500 hover:bg-red-dark text-white font-bold py-2 px-4 rounded" value="취소"/>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>
<%--<input onchange="console.log(this.value); console.log(this.value.length - 'C:\\fakepath\\'.length);" type="file">--%>
<script>
	$(document).ready(function () {

		/* 파일 선택 클릭 시 */
	  $('.add-file-input').on('click', function (e) {
		  e.stopPropagation(); // 상위 버블링 막기
		  // e.stopImmediatePropagation(); // 같은 이벤트에 대해 바인딩된 다른 콜백 막기

		  let $inputFileWrap = $(this).parent('div.input-file-wrap');
	    let $existingFileItem = $inputFileWrap.find('div.existing-file-item'); // 기존 파일 노출 영역
		  // console.log($existingFileItem[0]);

		  if ($existingFileItem.length > 0) { // 기존 파일이 존재하면
			  return confirm('파일 변경 시 기존파일이 삭제됩니다.') ? true : false; // 확인시 파일 변경 진행, 취소시 종료
		  }
	  });

	  /* 파일 변경 시 */
	  $('.add-file-input').on('change', function (e) {
	    let fileLength = this.files.length; // 새로 첨부된 파일 개수 (파일이 없으면 0)

		  let $inputFileWrap = $(this).parent('div.input-file-wrap');
	    let $existingFileItem = $inputFileWrap.find('div.existing-file-item'); // 기존 파일 노출 영역
		  let $deleteCheckbox = $existingFileItem.find('input[type=checkbox][name*=deleteFile]'); // 기존파일 삭제 체크박스

	    // 첨부된 파일(fileLength)이 있으면 기존파일에 삭제 체크, 없으면 무조건 기존파일 삭제 체크 취소해서 기존파일 그대로 유지
		  $deleteCheckbox.prop('checked', function(index, oldPropertyValue) {
			  return (fileLength > 0) ? true : false; // 비어있는 파일로의 변경도 이벤트로 간주하므로, 이때는 기존파일을 삭제 체크하지 않도록
		  });

	    // 기존 파일 노출 숨기기
	    $existingFileItem.hide();
	  });

	  /* 파일 취소 클릭 시 */
	  $('.cancel-file-input').on('click', function (e) {
		  if(! confirm('파일 첨부를 취소하시겠습니까?')) return false;

		  let $inputFileWrap = $(this).closest('div.input-file-wrap');
		  let $inputFile = $inputFileWrap.find('> input[type=file]'); // 첨부 파일 위치
		  let $existingFileItem = $inputFileWrap.find('div.existing-file-item'); // 기존 파일 노출 영역
		  let $deleteCheckbox = $inputFileWrap.find('input[type=checkbox][name*=deleteFile]'); // 기존파일 삭제 체크박스

		  // 첨부 파일 비우기
		  $inputFile.val('');

		  // 기존 파일 다시 노출
		  $existingFileItem.show();

		  // 기존파일 삭제체크 해제
		  $deleteCheckbox.prop('checked', false);

		  // 파일 취소 버튼 감추기
		  $(this).hide();

		  return false;
	  });

});
</script>
<%--<script>--%>
<%--    function readURL(input) {--%>
<%--        if (input.files && input.files[0]) {--%>
<%--            const reader = new FileReader();--%>

<%--            reader.readAsDataURL(input.files[0]);--%>

<%--            reader.onload = function (e) {--%>
<%--                $('#blah').attr('src', e.target.result);--%>
<%--            }--%>
<%--        }--%>
<%--    }--%>
<%--</script>--%>
<%@ include file="../layout/main_layout_foot.jspf" %>