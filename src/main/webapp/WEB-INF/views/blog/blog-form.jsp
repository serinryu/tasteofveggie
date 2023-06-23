<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<div class="container">
    <form action="" method="POST">
        <div class="row">
            <div class="col-3">
                <label for="writer" class="form-label">글쓴이</label>
                <input type="text" class="form-control" id="writer" name="writer" placeholder="글쓴이를 적어주세요.">
            </div>
            <div class="col-3">
                <label for="title" class="form-label">제목</label>
                <input type="text" class="form-control" id="title" name="blogTitle" placeholder="제목을 적어주세요.">
            </div>
        </div>
        <div class="row">
            <div class="col-6">
                <label for="content" class="form-label">본문</label>
                <textarea class="form-control" id="content" name="blogContent" rows="10"></textarea>
            </div>
        </div>
        <input type="submit">
    </form>
</div>
</body>
</html>