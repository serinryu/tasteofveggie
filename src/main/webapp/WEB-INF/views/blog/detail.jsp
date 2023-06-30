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
                ${blog.blogWriter}
            </div>
            <div class = "col-2">
                ${blog.blogCount}
            </div>
            <div class = "col-8">
                ${blog.blogContent}
            </div>
        </div>

    <div class="col-1">
        <form action="/blog/delete/${blog.blogId}" method="POST">
            <input type="hidden" name="blogId"> <!--value="${blog.blogId}"-->
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

    <!-- Modal for updating reply -->
    <div class="modal fade" id="replyUpdateModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">댓글 수정하기</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
    <script>
        let blogId = "${blog.blogId}"; // 글 번호를 자바스크립트 변수에 저장
        let $replies = document.getElementById("replies"); // 변수명 앞에 $ 를 하는 이유는 태그에 걸린 변수라는 의미를 담기 위함일 뿐
        let $replySubmit = document.getElementById("replySubmit");
        let $replyUpdateBtn = document.getElementById('replyUpdateBtn');

        /**
         * getAllReplies
         */
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
                            `<span id="replyId\${reply.replyId}">\${i+1}번째 댓글 ||
                                <span> 글쓴이:
                                    <span id="replyWriter\${reply.replyId}">\${reply.replyWriter}</span>
                                </span>
                                <span> 댓글내용:
                                    <span id="replyContent\${reply.replyId}">\${reply.replyContent}</span>
                                </span>
                                <span class="updateReplyBtn" data-replyId="\${reply.replyId}"
                                    data-bs-toggle="modal" data-bs-target="#replyUpdateModal">
                                    [수정]
                                </span>
                                <span class="deleteReplyBtn" data-replyId="\${reply.replyId}">
                                    [삭제]
                                </span>
                            </span>`;
                    });
                    // 저장된 #replies의 innerHTML 에 str을 대입해 실제 화면에 출력되게 해주세요.
                    $replies.innerHTML = str;
                })
                .catch((error) => {
                    console.error('Error while deleting the reply:', error);
                })
        }

        /**
         * deleteReply()
         */
        function deleteReply(replyId) {

            if (confirm("정말로 삭제하시겠어요?")) {

                // Send a DELETE request to the server
                let url = `http://localhost:8080/reply/\${replyId}`;
                fetch(url, {
                    method: 'delete'
                }).then((res) => {
                    if (res.ok) {
                        // Reply deleted successfully, update the UI
                        const replyElement = document.getElementById(`replyId\${replyId}`);
                        if (replyElement) {
                            replyElement.remove();
                        }
                        alert('해당 댓글을 삭제했습니다.');
                        //  혹은 전체 댓글 갱신도 가능
                        // getAllReplies(blogId);
                    } else {
                        // Failed to delete the reply, handle the error
                        console.error('Failed to delete the reply:', res.status);
                    }
                }).catch((error) => {
                    console.error('Error while deleting the reply:', error);
                });
            }
        }

        /**
         * insertReply()
         */
        function insertReply(){
            if(document.getElementById("replyWriter").value.trim() === ""){
                alert("글쓴이를 채워주셔야 합니다.");
                return;
            }
            if(document.getElementById("replyContent").value.trim() === ""){
                alert("본문을 채워주셔야 합니다.");
                return;
            }

            // Send a INSERT request to the server
            let url = `http://localhost:8080/reply`;
            fetch(url, {
                method:'post',
                headers: {
                    // header에는 보내는 데이터의 자료형에 대해서 기술
                    // json 데이터를 요청과 함께 전달, @RequestBody를 입력받는 로직에 추가
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    // 요청과 함께 보낼 json정보
                    replyWriter: document.getElementById("replyWriter").value,
                    replyContent: document.getElementById("replyContent").value,
                    blogId : "${blog.blogId}"
                }), // insert 로직이기 때문에 response에 실제 화면에서 사용할 데이터 전송 X
            }).then((res) => {
                if(res.ok) {
                    // Reply created successfully, update the UI
                    document.getElementById("replyWriter").value = ""; // 댓글 작성 후 폼에 작성되어있던 내용 소거
                    document.getElementById("replyContent").value = "";
                    alert("댓글 작성이 완료되었습니다!");
                    // 댓글 갱신
                    getAllReplies(blogId);
                } else {
                    // Failed to insert the reply, handle the error
                    console.error('Failed to insert the reply:', res.status);
                }
            }).catch((error) => {
                console.error('Error while deleting the reply:', error);
            });
        }

        /**
         * openUpdateReplyModal()
         */
        function openUpdateReplyModal(replyId){

            const $modalReplyId = document.getElementById("modalReplyId");
            $modalReplyId.value = replyId; // put replyId in hidden tag(#modalReplyId)

            const $replyContent = document.getElementById(`replyContent\${replyId}`);

            let existingReplyContent = $replyContent.innerText; // 태그는 제거하고 내부 문자만 추출

            // put data inside modal
            const $modalReplyContent = document.getElementById("modalReplyContent");
            $modalReplyContent.value =  existingReplyContent;
        }

        /**
         * updateReply()
         */
        function updateReply(replyId){
            let url = `http://localhost:8080/reply/\${replyId}`;
            fetch(url, {
                method: 'put',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    replyContent: document.querySelector("#modalReplyContent").value
                }),
            }).then((res) => {
                if(res.ok){
                    // Reply updated successfully, update the UI
                    document.getElementById("replyContent").value = "";
                    getAllReplies(blogId); // 갱신
                } else {
                    // Failed to update the reply, handle the error
                    console.error('Failed to update the reply:', res.status);
                }
            }).catch((error) => {
                    console.error('Error while updating the reply:', error);
            })
        }


        /**
         * Invoke the functions
         */

        getAllReplies(blogId);
        $replySubmit.addEventListener("click", insertReply);

        $replies.addEventListener("click", (e) => {
            const replyId = e.target.dataset['replyid']; // e.target.dataset.replyid; 위와 동일
            if(e.target.matches('#replies .deleteReplyBtn')){ // 이벤트객체.target.matches는 클릭한 요소가 어떤 태그인지 검사
                deleteReply(replyId);
            } else if(e.target.matches('#replies .updateReplyBtn')){
                openUpdateReplyModal(replyId);
            } else {

            }
        });

        $replyUpdateBtn.addEventListener("click", (e) => {
            const $modalReplyId = document.getElementById("modalReplyId"); // data inside hidden tag
            const replyId = $modalReplyId.value;
            updateReply(replyId);
        })

    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-geWF76RCwLtnZ8qwWowPQNguL3RmwHVBC9FhGdlKrxdiJJigb/j/68SIy3Te4Bkz" crossorigin="anonymous"></script>
</body>
</html>