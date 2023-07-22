
// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('blog-id').value;
        fetch(`/api/blogs/${id}`, {
            method: 'DELETE'
        })
            .then(() => {
                alert('Successfully deleted!');
                location.replace('/blogs');
            });
    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/blogs/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                blogTitle: document.getElementById('title').value,
                blogContent: document.getElementById('content').value
            })
        })
        .then(() => {
            alert('Successfully updated!');
            location.replace(`/blogs/${id}`);
        });
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        fetch('/api/blogs', {
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
            alert('Successfully uploaded!');
            location.replace('/blogs');
        });
    });
}
