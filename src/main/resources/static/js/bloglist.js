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
            // 쿠키에서 액세스 토큰 값을 가져와 헤더에 추가
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refreshToken = getCookie('refresh_token');

        if (response.status === 401 && refreshToken) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    // 쿠키에서 액세스 토큰 값을 가져와 헤더에 추가
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
                .then(result => { // 재발급이 성공하면 새로운 액세스 토큰으로 교체
                    document.cookie = `access_token=${result.accessToken}`;
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}

httpRequest('GET', '/blogs', null,
    function successCallback() {
        console.log('Request succeeded');
    },
    function errorCallback() {
        console.log('Request failed');
    }
);