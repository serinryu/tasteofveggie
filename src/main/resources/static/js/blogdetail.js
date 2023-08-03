const blogContentDiv = document.getElementById('blogContent');
const blogTitleElem = blogContentDiv.querySelector('h1');
const blogWriterElem = blogContentDiv.querySelector('.text-muted.fst-italic.mb-2:nth-child(2)');
const blogViewElem = blogContentDiv.querySelector('.text-muted.fst-italic.mb-2:nth-child(3)');
const blogContentElem = blogContentDiv.querySelector('.fs-5.mb-4');

// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });

    return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    // Get the access_token from local storage
    const accessToken = localStorage.getItem('access_token');
    console.log(accessToken);

    // IsLoggedIn? = Have accessToken?
    // 인증되지 않은 사용자는 /api/** 로 fetch 요청을 보내지 않고, /login 으로 이동
    if(!accessToken){
        window.location.replace('/login');
        throw new Error('Unauthorized');
    }

    fetch(url, {
        method: method,
        headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return response.json(); // Parse the response body as JSON and return it as a Promise
        }
        const refreshToken = getCookie('refresh_token');

        if (response.status === 401 && refreshToken) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: refreshToken,
                }),
            })
                .then(res => {
                    if (res.ok) {
                        return res.json();
                    }
                })
                .then(result => {
                    document.cookie = `access_token=${result.accessToken}`;
                    // Call the httpRequest function again with the updated access_token
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    }).then(data => {
        success(data); // Call the success callback with the parsed data
    }).catch(error => {
        fail();
    });
}

// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('blog-id').value;
        function success() {
            console.log('삭제가 완료되었습니다.');
            location.replace('/blogs');
        }

        function fail() {
            console.log('삭제 실패했습니다.');
            location.replace('/blogs');
        }

        httpRequest('DELETE',`/api/blogs/${id}`, null, success, fail);
    });
}

// 수정 기능
const modifyBtn = document.getElementById('modify-btn-new');
if(modifyBtn) {
    modifyBtn.addEventListener('click', function () {
        // Get the blog ID from the hidden input
        const blogId = document.getElementById('blog-id').value;

        // Get the existing blog data
        const blogTitle = blogTitleElem.textContent;
        const blogContent = blogContentElem.textContent;

        // Create a URL with query parameters to pass existing data to the new modify page
        const modifyUrl = `/blogs/new?id=${blogId}`;
        window.location.href = modifyUrl;
    });
}

// Fetch the blog data and update the content
function fetchBlogData(blogId) {
    httpRequest('GET', `/api/blogs/${blogId}`, null,
        function successCallback(data) {

        console.log(data);
            // Update the content with the received data
            blogTitleElem.textContent = data.blogTitle;
            blogWriterElem.textContent = `By : ${data.blogWriter}`;
            blogViewElem.textContent = `View : ${data.blogCount}`;
            blogContentElem.textContent = data.blogContent;

            // Set the blog ID as the value of the hidden input
            const blogIdInput = blogContentDiv.querySelector('#blog-id');
            blogIdInput.value = data.blogId;

        },
        function errorCallback() {
            console.log('Request failed');
        }
    );
}

const currentBlogId = window.location.pathname.split('/').pop();
fetchBlogData(currentBlogId);