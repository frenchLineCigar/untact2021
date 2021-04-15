<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../layout/main_layout_head.jspf" %>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto px-3 py-2 mt-8">
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

</section>

<%@ include file="../layout/main_layout_foot.jspf" %>