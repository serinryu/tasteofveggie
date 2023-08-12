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
    <h4 id="name" class="mb-3 text-right"></h4>
    <button type="button" class="btn btn-secondary btn-sm mb-3 text-right" onclick="performLogout()">로그아웃</button>
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
        <tbody id="blogTableBody">
        <!-- Dynamic table rows will be added here -->
        </tbody>
    </table> <!-- .table table-hover-->

    <div class="col-10"> <!-- pagination -->
        <ul id="pagination" class="pagination justify-content-center">
            <!-- Dynamic pagination buttons will be added here -->
        </ul>
    </div>

    <button type="button" id="create-btn"
            onclick="location.href='${pageContext.request.contextPath}/blogs/new'"
            class="btn btn-secondary btn-sm mb-3"> 글쓰기 </button>

</div> <!-- .container -->

<script src="/js/token.js"></script>
<script src="/js/bloglist.js"></script>

</body>
</html>
