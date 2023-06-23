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

    <!--comment-->
    <div class="row">
        <div id="replies">

        </div>
    </div>

    <!--comment form-->
    <div class="row">
        <!-- 비동기 form의 경우는 목적지로 이동하지 않고 페이지 내에서 처리가 되므로
        action을 가지지 않습니다. 그리고 제출버튼도 제출기능을 막고 fetch 요청만 넣습니다.-->
        <div class="col-2">
            <input type="text" class="form-control" id="replyWriter" name="replyWriter">
        </div>
        <div class="col-6">
            <input type="text" class="form-control" id="replyContent" name="replyContent">
        </div>
        <div class="col-2">
            <button class="btn btn-primary" id="replySubmit">댓글쓰기</button>
        </div>
    </div>

    </div><!-- .container -->
    <script>
        let blogId = "${blog.blogId}"; // 글 번호를 자바스크립트 변수에 저장

        console.log(blogId);

        // blogId 를 받아 전체 데이터를 JS 내부로 가져오는 함수 선언
        function getAllReplies(id){
            // <%-- jsp와 js가 모두 ${변수명} 문법을 공유하고, 이 중 .jsp파일에서는
            // ${}의 해석을 jsp식으로 먼저 하기 때문에, 해당 ${}가 백틱 내부에서 쓰이는 경우
            // \${} 형식으로 \를 추가로 왼쪽에 붙여서 jsp용으로 작성한 것이 아님을 명시해야함. --%>
            let url = `http://localhost:8080/reply/\${id}/all`;

            let str = "";

            fetch(url, {method:'get'}) // get 방식으로 위 주소에 요청 넣기
                .then((res)=>res.json()) // 응답 받은 요소 중 json 만 처리
                .then(replies => {
                    replies.map((reply, i) => {     // reply : target to map, i : order
                        str +=
                            `<h3>\${i+1}번째 댓글 || 글쓴이: \${reply.replyWriter}, 댓글내용: \${reply.replyContent}
                                <span class="deleteReplyBtn" data-replyId="\${reply.replyId}">
                                    [삭제]
                                </span>
                            </h3>`;
                    });

                    // 변수명 앞에 $ 를 하는 이유는 태그에 걸린 변수라는 의미를 담기 위함일 뿐
                    const $replies = document.getElementById('replies');
                    // 저장된 #replies의 innerHTML 에 str을 대입해 실제 화면에 출력되게 해주세요.
                    $replies.innerHTML = str;

                });
        }

        getAllReplies(blogId);

    </script>


</body>
</html>