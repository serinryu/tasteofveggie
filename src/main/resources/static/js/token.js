// /blogList 에 들어왔을때 실행되는 자바스크립트
// 로컬스토리지에 access token 을 저장해둔다.

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
    window.location.href = '/login';
}
