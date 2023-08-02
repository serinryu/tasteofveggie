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

const insertBlog = document.getElementById('insert-blog');

if (insertBlog) {
    insertBlog.addEventListener('click', event => {

        const classNumber = insertBlog.classList[0];
        httpRequest('GET',`/api/blogs?page=${classNumber}`, null,
            function successCallback(data) {
                console.log(data);
            },
            function errorCallback() {
                console.log('Request failed');
            }
        );
    });
}


const name = document.getElementById('name');
httpRequest('GET',`/api/blogs?page=1`, null,
    function successCallback(data) {
        console.log(data);
        const username = data.username;
        const nameDiv = document.getElementById('name');
        nameDiv.innerText = JSON.stringify(username);
    },
    function errorCallback() {
        console.log('Request failed');
    }
);