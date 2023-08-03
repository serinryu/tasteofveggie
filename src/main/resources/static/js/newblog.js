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
        return; // Stop the function here to prevent further execution
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
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return response.json(); // Parse the response body as JSON and return it as a Promise
            } else {
                return response; // if the response is empty or not JSON
            }
        }

        const refreshToken = getCookie('refresh_token');

        if (response.status === 401 && refreshToken) {
            fetch('/api/issue-new-token', {
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
                    //document.cookie = `access_token=${result.accessToken}`;
                    localStorage.setItem('access_token', newAccessToken);
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

        // Call the httpRequest function with the PUT method
        httpRequest('PUT', `/api/blogs/${id}`, body,
            (responseData) => {
                // Success callback, handle the success scenario here
                console.log('Blog updated successfully.');
                location.replace(`/blogs/${id}`);
            },
            (error) => {
                // Error callback, handle the error scenario here
                console.error('Error while updating the blog:', error);
                //location.replace(`/blogs/${id}`);
            }
        );

    });
}
