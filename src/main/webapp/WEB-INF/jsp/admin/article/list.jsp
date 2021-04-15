<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="../layout/main_layout_head.jspf" %>


<section class="section-1">
    <div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
        <c:forEach items="${articles}" var="article">
            <div>
                ${article.id} / ${article.regDate} / ${article.title}
            </div>
        </c:forEach>
    </div>
</section>

<%@ include file="../layout/main_layout_foot.jspf" %>