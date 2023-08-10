// 소셜 로그인 시 blogList 로 뒤에 토큰 붙어서 들어옴 (http://localhost:8080?token=dewfw)
// URL param 뒤에 토큰이 붙어서 들어올때 사용되는 코드

const token = searchParam('token')
if (token) {
    localStorage.setItem("access_token", token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}


function performLogout() {
    // Remove the 'access_token' from local storage
    localStorage.removeItem('access_token');
    window.location.href = '/logout';
}
