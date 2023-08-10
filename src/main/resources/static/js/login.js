function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const formData = {
        email: email,
        password: password
    };

    fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {
            const accessToken = data.access_token;

            // access_token을 localStorage에 저장
            // localStorage의 경우, 직접 초기화(clear)하거나 제거(removeItem)하지 않는다면 만료기간이 존재하지 않음. (브라우저를 닫아도 반영구적으로 저장)
            localStorage.setItem('access_token', accessToken);

            // 로그인 성공 시 /blogs 로 이동
            window.location.href = '/blogs';
        })
        .catch(error => {
            console.error('Error:', error);
        });
}