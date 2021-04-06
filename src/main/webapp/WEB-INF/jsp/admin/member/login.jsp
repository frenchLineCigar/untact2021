<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Title</title>
	<%-- tailwindcss : A utility-first CSS framework for rapidly building custom user interfaces. --%>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tailwindcss/2.1.1/tailwind.min.css" integrity="sha512-BAK6UB671tmfzrkeH1CacTvgHQ3aLAFnT2KsigdATsc5X7+3u42tb5vjmAoDiqtxphP5dNZ3cDygivTsGEJhGw==" crossorigin="anonymous" />
</head>
<body>
	<script>
		// 중복 submit 막기위해서 폼전송이 수행됐는지 여부를 담기위한 변수
		let LoginForm__checkAndSubmitDone = false;
        function LoginForm__checkAndSubmit(form) {
            // 폼이 한번 보내진 상태면 바로 함수 종료시킴
            if ( LoginForm__checkAndSubmitDone ) {
                return;
            }

            form.loginId.value = form.loginId.value.trim();

            if ( form.loginId.value.length == 0 ) {
                alert('로그인 아이디를 입력해주세요.');
                form.loginId.focus();

                return;
            }

            if ( form.loginPw.value.length == 0 ) {
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
		<div class="container mx-auto">
			<form action="doLogin" method="post" onsubmit="LoginForm__checkAndSubmit(this); return false;">
				<div class="flex">
					<div class="p-4 w-36">
						<span class="flex-grow">로그인 아이디</span>
					</div>
					<div class="flex-grow p-4">
						<input class="w-full" autofocus="autofocus" type="text"
						       placeholder="로그인 아이디를 입력해주세요." name="loginId" maxlength="20" />
					</div>
				</div>
				<div class="flex">
					<div class="p-4 w-36">
						<span class="flex-grow">로그인 비밀번호</span>
					</div>
					<div class="flex-grow p-4">
						<input class="w-full" autofocus="autofocus" type="password"
						       placeholder="로그인 비밀번호를 입력해주세요." name="loginPw" maxlength="20" />
					</div>
				</div>
				<div class="flex">
					<div class="p-4 w-36">
						<span>로그인</span>
					</div>
					<div class="flex-grow p-4">
						<input class="w-full" type="submit" value="로그인" />
					</div>
				</div>
			</form>
		</div>
	</section>

</body>
</html>