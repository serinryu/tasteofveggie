<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <!-- CSS only -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
    <div class="container">
        <h1 class="text-center"> Blog List </h1>
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
            <c:forEach var="blog" items="${blogList}">
                <tr>
                    <td>${blog.blogId}</td>
                    <td>${blog.blogWriter}</td>
                    <td><a href="/blog/detail/${blog.blogId}">${blog.blogTitle}</a></td>
                    <td>${blog.blogContent}</td>
                    <td>${blog.publishedAt}</td>
                    <td>${blog.updatedAt}</td>
                    <td>${blog.blogCount}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table> <!-- .table table-hover-->
        <div>
            <a href="/blog/create" class="btn btn-primary">글쓰기</a>
        </div>
    </div><!-- .container -->
</body>
</html>