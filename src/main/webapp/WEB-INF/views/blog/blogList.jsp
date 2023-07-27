<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Blog List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>

<div class="p-5 bg-light">
    <h1 class="mb-3">My Blog</h1>
    <h4 class="mb-3 text-right">Hello, ${pageContext.request.remoteUser} </h4>
    <button type="button" class="btn btn-secondary btn-sm mb-3 text-right" onclick="location.href='/logout'">로그아웃</button>
</div>

<div class="container">
    <table class="table table-striped table-hover">
        <thead>
            <th>blogId</th>
            <th>blogWriter</th>
            <th>blogTitle</th>
            <th>blogContent</th>
            <th>publishedAt</th>
            <th>updatedAt</th>
            <th>blogCount</th>
        </thead>
        <tbody>
        <c:forEach var="blog" items="${pageInfo.toList()}">
            <tr>
                <td>${blog.blogId}</td>
                <td>${blog.blogWriter}</td>
                <td><a href="/blogs/${blog.blogId}">${blog.blogTitle}</a></td>
                <td>${blog.blogContent}</td>
                <td>${blog.publishedAt}</td>
                <td>${blog.updatedAt}</td>
                <td>${blog.blogCount}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table> <!-- .table table-hover-->

    <div class="col-10"> <!-- pagination -->
        <ul class="pagination justify-content-center">

            <!-- 이전페이지 버튼 -->
            <c:if test="${startPageNum != 1}">
                <li class="page-item">
                    <a class="page-link" href="/blogs?page=${startPageNum - 1}">이전으로</a>
                </li>
            </c:if>

            <!-- 번호 버튼 -->
            <c:forEach begin="${ startPageNum }"
                       end="${ endPageNum }"
                       var="btnNum">
                <li class="page-item ${ currentPageNum == btnNum ? 'active' : '' }">
                    <a class="page-link" href="/blogs?page=${btnNum}">${btnNum}</a>
                </li>
            </c:forEach>

            <!-- 다음페이지 버튼 -->
            <c:if test="${endPageNum != pageInfo.getTotalPages()}">
                <li class="page-item">
                    <a class="page-link" href="/blogs?page=${endPageNum + 1}">다음으로</a>
                </li>
            </c:if>

        </ul>
    </div>

    <button type="button" id="create-btn"
            onclick="location.href='${pageContext.request.contextPath}/blogs/new'"
            class="btn btn-secondary btn-sm mb-3"> 글쓰기 </button>

</div> <!-- .container -->

<script src="/js/token.js"></script>
<script src="/js/article.js"></script>

</body>
</html>