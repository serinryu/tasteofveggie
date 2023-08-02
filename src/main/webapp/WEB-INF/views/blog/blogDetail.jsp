<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Detail</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
</head>
<body>
    <div class="p-5 mb-5 text-center</> bg-light">
        <h1 class="mb-3">My Blog</h1>
        <h4 class="mb-3">블로그에 오신 것을 환영합니다.</h4>
    </div>


    <div class="container mt-5">
        <div id="blogContent" class="row">
            <div class="col-lg-8">
                <article>
                    <input type="hidden" id="blog-id" value="" />
                    <header class="mb-4">
                        <h1 class="fw-bolder mb-1"></h1>
                        <div class="text-muted fst-italic mb-2"> By : </div>
                        <div class="text-muted fst-italic mb-2"> View : </div>
                    </header>
                    <section class="mb-5">
                        <p class="fs-5 mb-4"></p>
                    </section>
                    <button type="button" id="modify-btn-new" class="btn btn-primary btn-sm">수정</button>
                    <button type="button" id="delete-btn" class="btn btn-secondary btn-sm">삭제</button>
                </article>
            </div>
        </div>

        <!--comment-->
        <div class="row">
            <div id="replies">

            </div>
        </div>

    <!--comment form-->
    <div class="row">
<%--        <div class="col-md-4 mb-2">--%>
<%--            <input type="text" class="form-control" id="replyWriter" name="replyWriter" placeholder="Your Name">--%>
<%--        </div>--%>
        <div class="col-md-6 mb-2">
            <input type="text" class="form-control" id="replyContent" name="replyContent" placeholder="Your Comment">
        </div>
        <div class="col-md-2 mb-2">
            <button class="btn btn-primary btn-block" id="replySubmit">Post Comment</button>
        </div>
    </div>

    </div>

    <!-- Modal for updating reply -->
    <div class="modal fade" id="replyUpdateModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">댓글 수정하기</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"> X </button>
                </div>
                <div class="modal-body">
                    댓글내용 : <input type="text" class="form-control" id="modalReplyContent">
                    <input type="hidden" id="modalReplyId" value="">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal" id="replyUpdateBtn">수정하기</button>
                </div>
            </div>
        </div>
    </div>

    </div><!-- .container -->

    <script src="/js/reply.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
    <script src="/js/blogdetail.js"></script>

</body>
</html>