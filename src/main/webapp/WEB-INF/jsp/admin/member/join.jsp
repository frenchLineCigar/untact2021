<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ include file="../layout/head.jspf" %>

<%-- Lodash --%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.21/lodash.min.js" integrity="sha512-WFN04846sdKMIP5LKNphMaWzU7YpMyCU245etK3g/2ARYbPK9Ub18eG+ljU96qKRCWh+quCY7yefSmlkQw1ANQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>

<script>

    // 중복 체크 유효성 검사를 통과한 아이디를 담을 변수
    let JoinForm__validLoginId = '';

    // 로그인 아이디 중복 체크 함수
    function JoinForm__checkLoginIdDuplicate() {
        const form = $('.join-form').get(0);

        form.loginId.value = form.loginId.value.trim();

        if (form.loginId.value.length == 0) { // 입력한 것이 없으면 ajax 전송 X
            return;
        }

	    $.get(
		    'checkLoginIdDuplicate',
		    {
			    loginId: form.loginId.value
		    },
		    function (data) {
			    let colorClass = 'text-green-500';

			    if (data.fail) {
				    colorClass = 'text-red-500';
				    form.loginId.focus();
			    } else {
				    JoinForm__validLoginId = data.body.loginId; // 서버 검증 후 유효한 아이디
			    }

			    $('.login-id-check-msg').html("<span class='" + colorClass + "'>" + data.msg + "</span>");
		    },
		    'json'
	    );

    }

    // 중복 submit 막기위해서 폼전송이 수행됐는지 여부를 담기위한 변수
    let JoinForm__checkAndSubmitDone = false;

    function JoinForm__checkAndSubmit(form) {
        // 폼이 한번 보내진 상태면 바로 함수 종료시킴
        if (JoinForm__checkAndSubmitDone) {
            return;
        }

        // 공백 제거
        form.loginId.value = form.loginId.value.trim();
        form.loginPw.value = form.loginPw.value.trim();
        form.name.value = form.name.value.trim();
        form.email.value = form.email.value.trim();
        form.cellphoneNo.value = form.cellphoneNo.value.trim();

        if (form.loginId.value.length == 0) {
            alert('로그인 아이디를 입력해주세요.');
            form.loginId.focus();

            return;
        }

        // 중복 체크 여부 검사 : 중복 체크 완료된 로그인 아이디와 다를 경우 (값을 다시 변경할 경우)
        if (form.loginId.value != JoinForm__validLoginId) {
	        alert('로그인 아이디를 중복체크를 해주세요.');
	        form.loginId.focus();

	        return;
        }

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

        if (form.name.value.length == 0) {
	        alert('이름을 입력해주세요.');
            form.name.focus();

            return;
        }

        if (form.nickname.value.length == 0) {
	        alert('별명을 입력해주세요.');
            form.nickname.focus();

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

	    // 폼 전송 함수 - 업로드 성공 시 콜백
	    const submitForm = function (data) {

		    if (data) {
			    form.fileIdsStr.value = data.body.fileIdsStr;
		    }

		    form.submit();

		    JoinForm__checkAndSubmitDone = true;
	    };

	    // 에러 메시지 출력 함수 - 업로드 실패 시 콜백
	    const printFallBackMsg = function (jqXHR, textStatus, errorThrown) {
		    alert(jqXHR.responseText);
		    console.log(jqXHR);
		    console.log(jqXHR.responseText);
		    console.log(jqXHR.status);

		    ArticleAdd__isSubmitted = false;
	    };

	    // 파일 업로드 함수 - 가장 먼저 파일 업로드 이후 성공 또는 실패 콜백 실행
	    function startUpload(onSuccess, onFailure) {

		    if (! form.file__member__0__common__attachment__1.value) { // 첨부파일이 없으면 즉시 폼전송 후 종료
			    onSuccess();
			    return;
		    }

		    // const formData = new FormData();
		    // formData.append("file__member__0__common__attachment__1", form.file__member__0__common__attachment__1.value.files[0]);
		    const fileUploadFormData = new FormData(form);

		    $.ajax({
			    url: '/common/file/doUpload',
			    data: fileUploadFormData,
			    processData: false, // multipart/form-data 전송 시
			    contentType: false, // multipart/form-data 전송 시
			    dataType: 'JSON',
			    type: 'POST',
			    success: onSuccess, // 업로드 성공시 콜백 -> startSubmitForm
			    error: onFailure // 실패시 메시지 -> printFallBackMsg
		    });

		    // 파일을 업로드 한 후
		    // 기다린다.
		    // 응답을 받는다.
		    // onSuccess를 실행한다.
	    }

	    // 파일 업로드 시작
	    startUpload(submitForm, printFallBackMsg);
    }

    $(function() {
	    $('.input-login-id').change(function () {
		    JoinForm__checkLoginIdDuplicate();
	    });

	    // 타이핑 시
	    $('.input-login-id').keyup(_.debounce(JoinForm__checkLoginIdDuplicate, 500));

	    // 붙여넣기 시
	    $('.input-login-id').on("paste", _.debounce(JoinForm__checkLoginIdDuplicate, 500));
    });
</script>
<section class="section-login">
    <div class="container mx-auto min-h-screen flex items-center justify-center">
        <div class="w-full">
            <%-- 로고 삽입 --%>
            <div class="logo-bar flex justify-center mt-3">
                <a href="#" class="logo">
                    <span>
                        <i class="fas fa-people-arrows"></i>
                    </span>
                    <span>UNTACT 21' ADMIN</span>
                </a>
            </div>
            <%-- / 로고 삽입 --%>
            <form class="join-form bg-white shadow-md rounded px-8 pt-6 pb-8 mt-4" action="doJoin" method="POST"
                  onsubmit="JoinForm__checkAndSubmit(this); return false;">
                <input type="hidden" name="fileIdsStr"/>
                <input type="hidden" name="redirectUrl" value="${param.redirectUrl}" />
                <div class="flex flex-col mb-4 mt-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>로그인 아이디</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="input-login-id shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                               autofocus="autofocus" type="text"
                               placeholder="로그인 아이디를 입력해주세요." name="loginId" maxlength="20"/>
                        <div class="login-id-check-msg"></div>
                    </div>
                </div>
                <div class="flex flex-col mb-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>로그인 비밀번호</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                               autofocus="autofocus" type="password"
                               placeholder="로그인 비밀번호를 입력해주세요." name="loginPw" maxlength="20"/>
                    </div>
                </div>
                <div class="flex flex-col mb-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>로그인 비밀번호 확인</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker mb-3"
                               autofocus="autofocus" type="password"
                               placeholder="로그인 비밀번호를 입력해주세요." name="loginPwConfirm" maxlength="20"/>
                    </div>
                </div>
                <div class="flex flex-col mb-4 mt-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>프로필 이미지</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input accept="image/gif, image/jpeg, image/png"
                               class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                               autofocus="autofocus" type="file" placeholder="프로필 이미지를 선택해주세요." maxlength="20"
                               name="file__member__0__common__attachment__1"/>
                    </div>
                </div>
                <div class="flex flex-col mb-4 mt-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>이름</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                               autofocus="autofocus" type="text"
                               placeholder="이름을 입력해주세요." name="name" maxlength="20"/>
                    </div>
                </div>
                <div class="flex flex-col mb-4 mt-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>별명</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                               autofocus="autofocus" type="text"
                               placeholder="별명을 입력해주세요." name="nickname" maxlength="20"/>
                    </div>
                </div>
                <div class="flex flex-col mb-4 mt-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>이메일</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                               autofocus="autofocus" type="email"
                               placeholder="이메일을 입력해주세요." name="email" maxlength="100"/>
                    </div>
                </div>
                <div class="flex flex-col mb-4 mt-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>휴대전화 번호</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                               autofocus="autofocus" type="tel"
                               placeholder="휴대전화 번호를 입력해주세요. (- 없이 입력해주세요.)" name="cellphoneNo" maxlength="11"/>
                    </div>
                </div>
                <div class="flex flex-col mb-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>로그인</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="btn-primary bg-blue-500 hover:bg-blue-900 text-white font-bold py-2 px-4 rounded"
                               type="submit" value="회원가입"/>
                        <a onclick="history.back();" class="btn-info bg-green-500 hover:bg-blue-900 text-white font-bold py-2 px-4 rounded inline-block">뒤로가기</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</section>

<%@ include file="../layout/foot.jspf" %>