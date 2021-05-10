<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.tena.untact2021.util.Util" %>

<%@ include file="../layout/main_layout_head.jspf" %>

<script>
    let MemberModify__isSubmitted = false; // 폼 전송 상태 (중복 전송 방지)

    function MemberModify__checkAndSubmit(form) {

	    // 폼이 한번 보내진 상태면 바로 함수 종료시킴
	    if (MemberModify__isSubmitted) {
		    return;
	    }

	    // 입력받은 패스워드가 있는 경우만
	    if(form.loginPw.value) {

	      form.loginPw.value = form.loginPw.value.trim();

	      if (form.loginPw.value.length == 0) {
		      alert('로그인 비밀번호를 입력해주세요.');
		      form.loginPw.focus();

		      return;
	      }

	      if (form.loginPwConfirm.value.length == 0) {
		      alert('비밀번호를 다시 한번 입력해주세요.');
		      form.loginPwConfirm.focus();

		      return;
	      }

	      if (form.loginPw.value != form.loginPwConfirm.value) {
		      alert('비밀번호가 서로 일치하지 않습니다.');
		      form.loginPw.focus();

		      return;
	      }

	    }

	    form.name.value = form.name.value.trim();
	    form.email.value = form.email.value.trim();
	    form.cellphoneNo.value = form.cellphoneNo.value.trim();

	    if (form.nickname.value.length == 0) {
		    alert('별명을 입력해주세요.');
		    form.nickname.focus();

		    return;
	    }

	    if (form.name.value.length == 0) {
		    alert('이름을 입력해주세요.');
		    form.name.focus();

		    return;
	    }

	    if (form.email.value.length == 0) {
		    alert('이메일을 입력해주세요.');
		    form.email.focus();

		    return;
	    }

	    if (form.cellphoneNo.value.length == 0) {
		    alert('휴대전화 번호를 입력해주세요.');
		    form.cellphoneNo.focus();

		    return;
	    }

	    form.submit();

	    // 폼 전송후 값 변경
	    MemberModify__isSubmitted = true;
    }
</script>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<form onsubmit="MemberModify__checkAndSubmit(this); return false;" action="doModify" method="POST">
			<input type="hidden" name="id" value="${member.id}"/>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
					<span>로그인 아이디</span>
				</div>
				<div class="lg:flex-grow">
					<span class="form-row-input w-full rounded-sm">${member.loginId}</span>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
					<span>로그인 비밀번호</span>
				</div>
				<div class="lg:flex-grow">
					<input type="password" name="loginPw" autofocus="autofocus"
					       class="form-row-input w-full rounded-sm" placeholder="로그인 시 비밀번호를 입력해주세요."/>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
					<span>로그인 비밀번호 확인</span>
				</div>
				<div class="lg:flex-grow">
					<input type="password" name="loginPwConfirm" autofocus="autofocus"
					       class="form-row-input w-full rounded-sm" placeholder="로그인 시 비밀번호를 다시 한번 입력해주세요."/>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
					<span>이름</span>
				</div>
				<div class="lg:flex-grow">
					<input value="${member.name}" type="text" name="name" autofocus="autofocus"
					       class="form-row-input w-full rounded-sm" placeholder="이름을 입력해주세요."/>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
					<span>별명</span>
				</div>
				<div class="lg:flex-grow">
					<input value="${member.nickname}" type="text" name="nickname" autofocus="autofocus"
					       class="form-row-input w-full rounded-sm" placeholder="별명을 입력해주세요."/>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
					<span>이메일</span>
				</div>
				<div class="lg:flex-grow">
					<input value="${member.email}" type="email" name="email" autofocus="autofocus"
					       class="form-row-input w-full rounded-sm" placeholder="이메일을 입력해주세요."/>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
					<span>권한 레벨</span>
				</div>
				<div class="lg:flex-grow">
					<select class="select-auth-level form-row-input rounded-sm">
						<option value="3">일반회원</option>
						<option value="7">관리자</option>
					</select>
					<script>
						const memberAuthLevel = parseInt("${member.authLevel}");
					</script>
					<script>
					  $('.section-1 .select-auth-level').val(memberAuthLevel);
					</script>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
					<span>연락처</span>
				</div>
				<div class="lg:flex-grow">
					<input value="${member.cellphoneNo}" type="tel" name="cellphoneNo" autofocus="autofocus"
					       class="form-row-input w-full rounded-sm" placeholder="연락처를 입력해주세요."/>
				</div>
			</div>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-40">
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