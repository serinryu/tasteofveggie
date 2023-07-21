
// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('blog-id').value;
        fetch(`/blog/delete/${id}`, {
            //method: 'DELETE'
            method: 'POST'
        })
            .then(() => {
                alert('삭제가 완료되었습니다.');
                location.replace('/blog/list');
            });
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/blog/update/${id}`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                blogTitle: document.getElementById('title').value,
                blogContent: document.getElementById('content').value
            })
        })
        .then(() => {
            alert('수정이 완료되었습니다.');
            location.replace(`/blog/detail/${id}`);
        });
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        fetch('/blog/create', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                blogTitle: document.getElementById('title').value,
                blogContent: document.getElementById('content').value
            })
        })
        .then(() => {
            alert('등록 완료되었습니다.');
            location.replace('/blog/list');
        });
    });
}
