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
        <h1 class="text-center"> ${blog.blogTitle} </h1>
        <div class="row first-row">
            <div class = "col-1">
                ${blog.blogId}
            </div>
            <div class = "col-1">
                ${blog.writer}
            </div>
            <div class = "col-2">
                ${blog.blogCount}
            </div>
            <div class = "col-8">
                ${blog.blogContent}
            </div>
        </div>
    </div><!-- .container -->
    <div class="col-1">
        <form action="/blog/delete" method="POST">
            <input type="hidden" name="blogId" value="${blog.blogId}">
            <input type="submit" value="삭제하기" class="btn btn-warning">
        </form>
    </div>
    <div class="col-1">
        <form action="/blog/updateform" method="POST">
            <input type="hidden" name="blogId" value="${blog.blogId}">
            <input type="submit" value="수정하기" class="btn btn-info">
        </form>
    </div>
</body>
</html>