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

// Fetch the blog data and update the content
function fetchPrevioudBlogData(blogId) {
    httpRequest('GET', `/api/blogs/new?id=${blogId}`, null,
        function successCallback(data) {

            console.log(data);

            const titleElem = document.getElementById('title');
            const contentElem = document.getElementById('content');

            titleElem.value = data.blogTitle;
            contentElem.value = data.blogContent;

            // Set the blog ID as the value of the hidden input
            const blogIdInput = document.getElementById('blog-id');
            blogIdInput.value = data.blogId;
        },
        function errorCallback() {
            console.log('Request failed');
        }
    );
}

// Show or hide buttons based on whether blogId exists
const createBtn = document.getElementById('create-btn');
const modifyBtn = document.getElementById('modify-btn');

// Get the blog ID from the URL parameter and fetch data
const urlParams = new URLSearchParams(window.location.search);
const blogId = urlParams.get('id');
if (blogId) {
    createBtn.style.display = 'none';
    modifyBtn.style.display = 'block';
    fetchPrevioudBlogData(blogId);
} else {
    createBtn.style.display = 'block';
    modifyBtn.style.display = 'none';
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        body = JSON.stringify({
            blogTitle: document.getElementById('title').value,
            blogContent: document.getElementById('content').value
        });

        function success() {
            console.log('등록 완료되었습니다.');
            location.replace('/blogs');
        };

        function fail() {
            console.log('등록 실패했습니다.');
            location.replace('/blogs');
        };

        httpRequest('POST','/api/blogs', body, success, fail)
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let id = document.getElementById('blog-id').value;

        body = JSON.stringify({
            blogTitle: document.getElementById('title').value,
            blogContent: document.getElementById('content').value
        })

        function success() {
            console.log('수정 완료되었습니다.');
            location.replace(`/blogs/${id}`);
        }

        function fail() {
            console.log('수정 실패했습니다.');
            location.replace(`/blogs/${id}`);
        }

        httpRequest('PUT',`/api/blogs/${id}`, body, success, fail);
    });
}
