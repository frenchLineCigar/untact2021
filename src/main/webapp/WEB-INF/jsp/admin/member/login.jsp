<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ include file="../layout/head.jspf" %>

<script>
    // 중복 submit 막기위해서 폼전송이 수행됐는지 여부를 담기위한 변수
    let LoginForm__checkAndSubmitDone = false;

    function LoginForm__checkAndSubmit(form) {
        // 폼이 한번 보내진 상태면 바로 함수 종료시킴
        if (LoginForm__checkAndSubmitDone) {
            return;
        }

        form.loginId.value = form.loginId.value.trim();

        if (form.loginId.value.length == 0) {
            alert('로그인 아이디를 입력해주세요.');
            form.loginId.focus();

            return;
        }

        if (form.loginPw.value.length == 0) {
            alert('로그인 비밀번호를 입력해주세요.');
            form.loginId.focus();

            return;
        }

        form.submit();
        // 폼 전송후 값 변경
        LoginForm__checkAndSubmitDone = true;
    }
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
            <form class="bg-white shadow-md rounded px-8 pt-6 pb-8 mt-4" action="doLogin" method="POST"
                  onsubmit="LoginForm__checkAndSubmit(this); return false;">
                <input type="hidden" name="redirectUrl" value="${param.redirectUrl}" />
                <div class="flex flex-col mb-4 mt-4 md:flex-row">
                    <div class="p-1 md:w-36 md:flex md:items-center">
                        <span>로그인 아이디</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
                               autofocus="autofocus" type="text"
                               placeholder="로그인 아이디를 입력해주세요." name="loginId" maxlength="20"/>
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
                        <span>로그인</span>
                    </div>
                    <div class="p-1 md:flex-grow">
                        <input class="bg-blue-500 hover:bg-blue-900 text-white font-bold py-2 px-4 rounded" type="submit"
                               value="로그인"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
</section>

<%@ include file="../layout/foot.jspf" %>