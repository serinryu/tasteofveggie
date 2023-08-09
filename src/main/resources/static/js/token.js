// /blogList 에 들어왔을때 실행되는 자바스크립트
// URL param 뒤에 토큰이 붙어서 들어올때 사용되는 코드

const token = searchParam('token')

console.log(token)

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
