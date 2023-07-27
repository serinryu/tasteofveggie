// /blogList 에 들어왔을때 실행되는 자바스크립트
// 로그인 후 한 번만 토큰을 로컬스토리지에 저장하면 된다.

const token = searchParam('token')

if (token) {
    localStorage.setItem("access_token", token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}