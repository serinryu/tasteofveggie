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
        }
        else {
            return fail();
        }
    }).then(data => {
        success(data); // Call the success callback with the parsed data
    }).catch(error => {
        fail();
    });
}


// 페이지네이션을 생성하는 함수
function createPagination(currentPageNum, startPageNum, endPageNum) {
    const paginationUl = document.getElementById('pagination');
    paginationUl.innerHTML = ''; // Clear the pagination list before adding new buttons

    // 이전페이지 버튼
    if (currentPageNum !== 1) {
        const prevPageBtn = document.createElement('li');
        prevPageBtn.className = 'page-item';
        const prevPageLink = document.createElement('a');
        prevPageLink.className = 'page-link';
        prevPageLink.textContent = '이전으로';
        prevPageLink.addEventListener('click', () => {
            fetchPageData(currentPageNum - 1);
        });
        prevPageBtn.appendChild(prevPageLink);
        paginationUl.appendChild(prevPageBtn);
    }

    // 번호 버튼
    for (let btnNum = startPageNum; btnNum <= endPageNum; btnNum++) {
        const pageNumBtn = document.createElement('li');
        pageNumBtn.className = `page-item ${currentPageNum === btnNum ? 'active' : ''}`;
        const pageNumLink = document.createElement('a');
        pageNumLink.className = 'page-link';
        pageNumLink.textContent = btnNum;
        pageNumLink.addEventListener('click', () => {
            fetchPageData(btnNum);
        });
        pageNumBtn.appendChild(pageNumLink);
        paginationUl.appendChild(pageNumBtn);
    }

    // 다음페이지 버튼
    if (endPageNum !== currentPageNum) {
        const nextPageBtn = document.createElement('li');
        nextPageBtn.className = 'page-item';
        const nextPageLink = document.createElement('a');
        nextPageLink.className = 'page-link';
        nextPageLink.textContent = '다음으로';
        nextPageLink.addEventListener('click', () => {
            fetchPageData(currentPageNum + 1);
        });
        nextPageBtn.appendChild(nextPageLink);
        paginationUl.appendChild(nextPageBtn);
    }
}

// Function to fetch data and update the table
function fetchPageData(pageNum) {
    httpRequest('GET', `/api/blogs?page=${pageNum}`, null,
        function successCallback(data) {
            console.log(data);
            // Received data from the API
            // Update the table with the received data
            const blogTableBody = document.getElementById('blogTableBody');
            blogTableBody.innerHTML = ''; // Clear the table body before adding new rows
            data.pageInfo.content.forEach(blog => {
                const row = blogTableBody.insertRow();
                row.insertCell().textContent = blog.blogId;
                row.insertCell().textContent = blog.blogWriter;
                row.insertCell().innerHTML = `<a href="/blogs/${blog.blogId}">${blog.blogTitle}</a>`;
                row.insertCell().textContent = blog.blogContent;
                row.insertCell().textContent = blog.publishedAt;
                row.insertCell().textContent = blog.updatedAt;
                row.insertCell().textContent = blog.blogCount;
            });

            // Create pagination buttons with the received data
            createPagination(data.currentPageNum, data.startPageNum, data.endPageNum);
        },
        function errorCallback() {
            console.log('Request failed');
        }
    );
}

const name = document.getElementById('name');
httpRequest('GET', `/api/blogs?page=1`, null,
    function successCallback(data) {
        const username = data.username;
        const nameDiv = document.getElementById('name');
        nameDiv.innerText = JSON.stringify(username);

        fetchPageData(1);
    },
    function errorCallback() {
        console.log('Request failed');
    }
);

