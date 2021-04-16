<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../layout/main_layout_head.jspf" %>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto px-3 py-2 mt-8">
		<%-- 게시판 변경 셀렉트 박스 --%>
		<div class="relative inline-flex mt-10 mx-5">
			<svg class="w-2 h-2 absolute top-0 right-0 m-4 pointer-events-none" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 412 232">
				<path d="M206 171.144L42.678 7.822c-9.763-9.763-25.592-9.763-35.355 0-9.763 9.764-9.763 25.592 0 35.355l181 181c4.88 4.882 11.279 7.323 17.677 7.323s12.796-2.441 17.678-7.322l181-181c9.763-9.764 9.763-25.592 0-35.355-9.763-9.763-25.592-9.763-35.355 0L206 171.144z" fill="#648299" fill-rule="nonzero"/>
			</svg>
			<select class="select-board-id border border-gray-300 rounded-full text-gray-600 h-10 pl-5 pr-10 bg-white hover:border-gray-400 focus:outline-none appearance-none">
				<option value="1">공지사항</option>
				<option value="2">자유게시판</option>
			</select>
			<script>
                const url = new URL(window.location.href);
                const urlParams = url.searchParams;
                const boardId = urlParams.has('boardId') && !util.isEmpty(urlParams.get('boardId')) ? urlParams.get('boardId') : 1; //1은 공지사항
                $('.section-1 .select-board-id').val(boardId);

                $('.section-1 .select-board-id').change(function () {
                    location.href = '?boardId=' + this.value;
                });
			</script>
		</div>
		<%-- / 게시판 변경 셀렉트 박스 --%>
		<%-- 게시물 리스트 --%>
		<div>
			<c:forEach items="${articles}" var="article">
				<div class="border-2 border-gray-50 p-4 mt-10 mx-5">
					<div class="flex justify-between items-center">
						<span class="font-light text-gray-600">${article.regDate}</span>
						<a href="list?boardId=${article.boardId}" class="px-2 py-1 bg-gray-600 text-gray-100 font-bold rounded hover:bg-gray-500">${article.extra__boardName}</a>
					</div>
					<div class="mt-2">
						<a href="detail?id=${article.id}" class="text-2xl text-gray-700 font-bold hover:underline">${article.title}</a>
						<p class="mt-2 text-gray-600">${article.body}</p>
					</div>
					<div class="flex justify-between items-center mt-4">
						<a href="detail?id=${article.id}" class="text-blue-500 hover:underline">자세히 보기</a>
						<div>
							<a href="detail?id=${article.id}" class="flex items-center">
								<img src="https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=731&amp;q=80" alt="avatar" class="mx-4 w-10 h-10 object-cover rounded-full">
								<h1 class="text-gray-700 font-bold hover:underline">${article.extra__writer}</h1>
							</a>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
		<%-- / 게시물 리스트 --%>
	</div>

</section>

<%@ include file="../layout/main_layout_foot.jspf" %>