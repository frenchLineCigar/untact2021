<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../layout/main_layout_head.jspf" %>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<div class="mb-10">
			<%-- 회원 리스트 --%>
			<c:forEach items="${members}" var="member">
				<c:set var="detailUrl" value="detail?id=${member.id}" />
				<div class="border-2 border-gray-50 p-2 mt-10">
					<div class="flex items-center">
						<a href="${detailUrl}" class="font-bold">NO. ${member.id}
							<span class="ml-2 font-light text-gray-600">${member.regDate}</span>
						</a>
						<div class="flex-grow"></div>
						<a class="px-2 py-1 bg-gray-600 text-gray-100 font-bold rounded hover:bg-gray-500">${member.authLevelName}</a>
					</div>
					<div class="member-preview mt-2">
						<%--<a href="${detailUrl}" class="text-2xl text-gray-700 font-bold hover:underline">${member.loginId}</a>--%>
						<p class="mt-2 text-gray-600">
							<span class="inline-flex justify-center items-center px-2 rounded-full bg-green-600 text-white">로그인 아이디</span>
							<span>${member.loginId}</span>
						</p>
						<p class="mt-2 text-gray-600">
							<span class="inline-flex justify-center items-center px-2 rounded-full bg-green-600 text-white">이름</span>
							<span>${member.name}</span>
						</p>
						<p class="mt-2 text-gray-600">
							<span class="inline-flex justify-center items-center px-2 rounded-full bg-green-600 text-white">닉네임</span>
							<span>${member.nickname}</span>
						</p>
					</div>
					<div class="flex items-center mt-4">
						<a href="detail?id=${member.id}" class="text-blue-500 hover:underline" title="자세히 보기">
							<span>
								<i class="fas fa-info-circle"></i>
								<span class="hidden sm:inline">자세히 보기</span>
							</span>
						</a>
						<a href="modify?id=${member.id}" class="ml-2 text-blue-500 hover:underline" title="수정">
							<span>
								<i class="fas fa-edit"></i>
								<span class="hidden sm:inline">수정</span>
							</span>
						</a>
						<a onclick="if( !confirm('삭제하시겠습니까?') ) return false;" href="doDelete?id=${member.id}" class="ml-2 text-blue-500 hover:underline" title="삭제">
							<span>
								<i class="fas fa-trash"></i>
								<span class="hidden sm:inline">삭제</span>
							</span>
						</a>
						<div class="flex-grow"></div>
						<div>
							<a class="flex items-center">
								<img src="https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=731&amp;q=80"
									 alt="avatar" class="mx-4 w-10 h-10 object-cover rounded-full">
								<h1 class="text-gray-700 font-bold hover:underline">${member.nickname}</h1>
							</a>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</section>
<script>
$(document).ready(function () {

    let $preview = $(".member-preview").find("p, img");

    $preview.mouseover(function () {
        $(this).css("text-decoration", "underline");
        $(this).css("cursor", "pointer");
    });

    $preview.mouseout(function () {
        $(this).css("text-decoration", "none");
    });

    let goDetailUrl = function (e, target) {
        let detailUrl = $(this).closest('div.member-preview').find('a').attr('href');
        location.href = detailUrl;
        // console.log(detailUrl);
        // console.log(this);
        // console.log(e);
        // console.log(target);
    };
    $preview.on("click", this, goDetailUrl)

});
</script>
<%@ include file="../layout/main_layout_foot.jspf" %>