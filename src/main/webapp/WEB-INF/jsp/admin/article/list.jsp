<%@ page import="com.tena.untact2021.util.Util" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../layout/main_layout_head.jspf" %>

<script>
	const boardId = parseInt("${board.id}");
	const searchKeywordType = "${search.searchKeywordType.value}";
	const searchKeyword = "${search.searchKeyword}";
</script>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<!-- 게시판 선택 및 글쓰기 버튼 -->
		<div class="flex items-center mt-10">
			<%-- 게시판 변경 드롭다운 --%>
			<select class="select-board-id py-2 px-2 rounded-full border border-gray-300 text-gray-600 bg-white hover:border-gray-400 focus:outline-none">
				<option value="1">공지사항</option>
				<option value="2">자유게시판</option>
			</select>
			<script>
				$('.section-1 .select-board-id').val(boardId);

				$('.section-1 .select-board-id').change(function () {
					location.href = '?boardId=' + this.value;
				});
			</script>
			<%-- 중앙 영역 점유 --%>
			<div class="flex-grow"></div>
			<%-- 글쓰기 버튼 --%>
			<a href="add?boardId=${board.id}" class="btn-primary add-article bg-blue-500 hover:bg-blue-900 text-white font-bold py-2 px-4 rounded-full">글쓰기</a>
		</div>
		<!-- /게시판 선택 및 글쓰기 버튼 -->

		<!-- 게시물 검색 박스 -->
		<div class="p-1 mt-5">총 게시물 수 : <c:out value="${Util.formatNumberWithComma(totalItemCount)} 건" /></div>
		<form class="flex mt-3">
			<select class="select-search-keyword-type" name="searchKeywordType">
				<option value="titleAndBody">전체</option>
				<option value="title">제목</option>
				<option value="body">본문</option>
			</select>
			<script>
				$('.section-1 .select-search-keyword-type').val(searchKeywordType);
			</script>
			<input class="ml-3 shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker" name="searchKeyword" type="text" placeholder="검색어를 입력해주세요." value="${param.searchKeyword}" />
			<input class="ml-3 btn-primary bg-blue-500 hover:bg-blue-900 text-white font-bold py-2 px-4 rounded" type="submit" value="검색"/>
		</form>
		<!-- /게시물 검색 박스 -->

		<!-- 게시물 리스트 -->
		<div class="mb-10">
			<c:forEach items="${articles}" var="article">
				<c:set var="detailUrl" value="detail?id=${article.id}" />
				<c:set var="thumbUrl" value="${article.extra__thumbUrl}" />
				<div class="border-2 border-gray-50 p-2 mt-10">
					<div class="flex items-center">
						<a href="${detailUrl}" class="font-bold">NO. ${article.id}
							<span class="ml-2 font-light text-gray-600">${article.regDate}</span>
						</a>
						<div class="flex-grow"></div>
						<a href="list?boardId=${article.boardId}"
						   class="px-2 py-1 bg-gray-600 text-gray-100 font-bold rounded hover:bg-gray-500">${article.extra__boardName}</a>
					</div>
					<div class="article-preview mt-2">
						<a href="${detailUrl}" class="text-2xl text-gray-700 font-bold hover:underline">${article.title}</a>
						<%-- 썸네일 표시 --%>
						<c:if test="${thumbUrl != null}">
							<div class="border-2 border-red-500">
								<img class="border-2 border-blue-500 max-w-sm" src="${thumbUrl}" alt="">
							</div>
							<%-- Beta --%>
							<c:if test="${article.extra.fileMap != null}">
								<c:set var="fileMap" value="${article.extra.fileMap}" />
								<c:set var="numberToDisplay" value="3" />
								<div class="border-2 border-red-500 max-w-sm flex">
									<c:forEach var="key" items="${fileMap}" varStatus="status">
										<c:set var="file" value="${key.value}" />
										<c:set var="anotherUrl" value="${file.forPrintUrl}" />
										<c:choose>
											<c:when test="${file.fileExtTypeCode == 'img'}">
												<img class="border-4 border-indigo-500 w-1/${numberToDisplay}" src="${anotherUrl}" alt="">
											</c:when>
											<c:otherwise>
												<img class="border-4 border-indigo-500 w-1/${numberToDisplay}" src="/resource/temp/no_image_available.png" alt="">
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</div>
								<div class="w-max">사이즈 : ${size}</div>
							</c:if>
							<%-- / Beta --%>
						</c:if>
						<p class="mt-2 text-gray-600">${article.body}</p>
					</div>
					<div class="flex items-center mt-4">
						<a href="detail?id=${article.id}" class="text-blue-500 hover:underline">자세히 보기</a>
						<a href="modify?id=${article.id}" class="ml-2 text-blue-500 hover:underline">수정</a>
						<a onclick="if( !confirm('삭제하시겠습니까?') ) return false;"
							 href="doDelete?id=${article.id}" class="ml-2 text-blue-500 hover:underline">삭제</a>
						<div class="flex-grow"></div>
						<div>
							<a class="flex items-center">
								<img src="https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=731&amp;q=80"
									 alt="avatar" class="mx-4 w-10 h-10 object-cover rounded-full">
								<h1 class="text-gray-700 font-bold hover:underline">${article.extra__writer}</h1>
							</a>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
		<!-- /게시물 리스트 -->

		<!-- Pagination -->
		<nav class="flex justify-center rounded-md" aria-label="Pagination">

			<!-- The First -->
			<a href="?page=1" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
				<span class="sr-only">First</span>
				<!-- Heroicon name: solid/chevron-double-left -->
				<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
					<path fill-rule="evenodd" d="M15.707 15.707a1 1 0 01-1.414 0l-5-5a1 1 0 010-1.414l5-5a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 010 1.414zm-6 0a1 1 0 01-1.414 0l-5-5a1 1 0 010-1.414l5-5a1 1 0 011.414 1.414L5.414 10l4.293 4.293a1 1 0 010 1.414z" clip-rule="evenodd" />
				</svg>
			</a>

			<!-- Previous -->
			<c:set var="prev" value="${(pageMenuStart - 1) - pageMenuArmSize}" />
			<a href="?page=${prev}" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
				<span class="sr-only">Previous</span>
				<!-- Heroicon name: solid/chevron-left -->
				<svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
					<path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
				</svg>
			</a>

			<!-- Page List -->
			<c:forEach var="i" begin="${pageMenuStart}" end="${pageMenuEnd}">
				<%--<c:set var="aClassStr" value="relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm font-medium" />--%>
				<c:set var="aClassStr" value="relative inline-flex items-center px-4 py-2 border text-sm font-medium" />
				<c:if test="${i == page}">
					<%--<c:set var="aClassStr" value="${aClassStr} text-red-500 hover:bg-red-50" />--%>
					<c:set var="aClassStr" value="${aClassStr} z-10 text-indigo-600 bg-indigo-50 border-indigo-500" />
				</c:if>
				<c:if test="${i != page}">
					<%--<c:set var="aClassStr" value="${aClassStr} text-gray-500 hover:bg-gray-50" />--%>
					<c:set var="aClassStr" value="${aClassStr} text-gray-500 bg-white border-gray-300 hover:bg-gray-50" />
				</c:if>
				<a href="?page=${i}" class="${aClassStr}">${i}</a>
			</c:forEach>

			<!-- Next -->
			<c:set var="next" value="${(pageMenuEnd + 1) + pageMenuArmSize}" />
			<a href="?page=${next}" class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
				<span class="sr-only">Next</span>
				<!-- Heroicon name: solid/chevron-right -->
				<svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
					<path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
				</svg>
			</a>

			<!-- The Last -->
			<a href="?page=${totalPage}" class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50">
				<span class="sr-only">Last</span>
				<!-- Heroicon name: solid/chevron-double-right -->
				<svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
					<path fill-rule="evenodd" d="M10.293 15.707a1 1 0 010-1.414L14.586 10l-4.293-4.293a1 1 0 111.414-1.414l5 5a1 1 0 010 1.414l-5 5a1 1 0 01-1.414 0z" clip-rule="evenodd" />
					<path fill-rule="evenodd" d="M4.293 15.707a1 1 0 010-1.414L8.586 10 4.293 5.707a1 1 0 011.414-1.414l5 5a1 1 0 010 1.414l-5 5a1 1 0 01-1.414 0z" clip-rule="evenodd" />
				</svg>
			</a>
		</nav>
		<!-- /Pagination -->

	</div>
</section>
<script>
$(document).ready(function () {

    let $preview = $(".article-preview").find("p, img");

    $preview.mouseover(function () {
        $(this).css("text-decoration", "underline");
        $(this).css("cursor", "pointer");
    });

    $preview.mouseout(function () {
        $(this).css("text-decoration", "none");
    });

    let goDetailUrl = function (e, target) {
        let detailUrl = $(this).closest('div.article-preview').find('a').attr('href');
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