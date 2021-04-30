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
    ArticleModify__isSubmitted = false; // 폼 전송 상태 (중복 전송 방지)
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
        const startUploadFiles = function (onSuccess) {

            // 첨부파일 변경 유무를 담을 변수
            let needToUpload = false;

						// 업로드할 파일 체크
            for (let fileNo = 1; fileNo <= ArticleModify__fileInputMaxCount; fileNo++) {
                const input = form["file__article__"+ articleId +"__common__attachment__" + fileNo];

                // 파일 크기 유효성 체크
                if (input.value.length > 0) { // 크기가 0보다 큰 것이 하나라도 있으면
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
                success: onSuccess // 업로드 성공시 콜백 -> startSubmitForm
            });
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

        startUploadFiles(startSubmitForm);
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

			<c:set var="fileMap" value="${article.fileMap}" />
			<c:forEach var="fileNo" begin="1" end="${fileInputMaxCount}">
				<c:set var="file" value="${fileMap[fileNo]}" />
				<div class="form-row flex flex-col lg:flex-row">
					<div class="lg:flex lg:items-center lg:w-28">
						<span>첨부파일 ${fileNo}</span>
					</div>
					<div class="lg:flex-grow input-file-wrap">
						<input type="file" name="file__article__${article.id}__common__attachment__${fileNo}" class="form-row-input rounded-sm" />
						<c:if test="${file != null}">
							<%-- 파일명/용량 표시--%>
							<div>
								<a href="${file.forPrintUrl}" target="_blank" class="text-blue-500 hover:underline">${file.originFileName}</a> (${file.fileSizeWithUnit})
							</div>
							<div>
								<label>
									<input onclick="$(this).closest('div.input-file-wrap').find('> input[type=file]').val('');" type="checkbox" name="deleteFile__article__${article.id}__common__attachment__${fileNo}" value="Y" />
									<span>삭제</span>
								</label>
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
						<input type="submit" class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded" value="수정"/>
						<input onclick="history.back()" type="button" class="btn-info bg-red-500 hover:bg-red-dark text-white font-bold py-2 px-4 rounded" value="취소"/>
					</div>
				</div>
			</div>
		</form>
	</div>
</section>
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