const apiUrl = "http://localhost:8080/reply";

const $replies = document.getElementById("replies");
const $replySubmit = document.getElementById("replySubmit");
const $replyUpdateBtn = document.getElementById("replyUpdateBtn");
const $modalReplyId = document.getElementById("modalReplyId");
const $modalReplyContent = document.getElementById("modalReplyContent");

const currentURL = window.location.href;
const blogIdMatch = currentURL.match(/blogs\/(\d+)/);
const blogId = blogIdMatch ? blogIdMatch[1] : null;

function getDateFormatted(timestamp) {
    const date = new Date(timestamp);
    return date.toLocaleString(); // Adjust this based on your desired date format
}

function getAllReplies(blogId) {
    const url = `${apiUrl}/${blogId}/all`;

    fetch(url)
        .then((res) => res.json())
        .then((replies) => {
            $replies.innerHTML = replies
                .map(
                    (reply, i) =>
                        `
                        <div class="card mt-5">
                        <div class="card-header">
                            <span class="font-weight-bold">${reply.replyWriter}</span>
                            <span class="text-muted">${getDateFormatted(reply.publishedAt)}</span>
                        </div>
                        
                        <div class="card-body">
                            <div class="card-text">${reply.replyContent}</div>
                            <button class="btn btn-primary btn-sm updateReplyBtn" data-replyId="${reply.replyId}" data-bs-toggle="modal" data-bs-target="#replyUpdateModal">
                            Edit
                            </button>
                            <button class="btn btn-secondary btn-sm deleteReplyBtn" data-replyId="${reply.replyId}">
                            Delete
                            </button>
                        </div>
                       </div>`
                )
                .join("");
        })
        .catch((error) => {
            console.error("Error while getting all replies:", error);
        });
}

function deleteReply(replyId) {
    if (confirm("Are you sure you want to delete this reply?")) {
        const url = `${apiUrl}/${replyId}`;

        fetch(url, { method: "delete" })
            .then((res) => {
                if (res.ok) {
                    const replyElement = document.getElementById(`replyId${replyId}`);
                    if (replyElement) {
                        replyElement.remove();
                    }
                    alert("Reply deleted successfully.");
                } else {
                    console.error("Failed to delete the reply:", res.status);
                }
            })
            .catch((error) => {
                console.error("Error while deleting the reply:", error);
            });
    }
}

function addReply() {
    //const replyWriter = document.getElementById("replyWriter").value.trim();
    const replyContent = document.getElementById("replyContent").value.trim();

    if (replyContent === "") {
        alert("Please fill Content fields.");
        return;
    }

    fetch(apiUrl, {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            replyContent,
            blogId,
        }),
    })
        .then((res) => {
            if (res.ok) {
                document.getElementById("replyContent").value = "";
                alert("Reply posted successfully!");
                getAllReplies(blogId); // UI update
            } else {
                console.error("Failed to insert the reply:", res.status);
            }
        })
        .catch((error) => {
            console.error("Error while inserting the reply:", error);
        });
}

function openUpdateReplyModal(replyId) {
    // Get the existing reply content
    const existingReplyContent = document.getElementById(`replyContent${replyId}`).innerText;

    // Populate the reply content inside the modal input field
    const $modalReplyContent = document.getElementById("modalReplyContent");
    $modalReplyContent.value = existingReplyContent;

    // Store the replyId in a hidden input field for later use during the update
    const $modalReplyId = document.getElementById("modalReplyId");
    $modalReplyId.value = replyId;
}

function updateReply(replyId) {
    const url = `${apiUrl}/${replyId}`;

    fetch(url, {
        method: "put",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            replyContent: $modalReplyContent.value,
        }),
    })
        .then((res) => {
            if (res.ok) {
                $modalReplyContent.value = "";
                getAllReplies("${blog.blogId}");
            } else {
                console.error("Failed to update the reply:", res.status);
            }
        })
        .catch((error) => {
            console.error("Error while updating the reply:", error);
        });
}

// Invoke the functions
getAllReplies(blogId);
$replySubmit.addEventListener("click", addReply);

$replies.addEventListener("click", (e) => {
    const replyId = e.target.dataset.replyid;
    if (e.target.matches(".deleteReplyBtn")) {
        deleteReply(replyId);
    } else if (e.target.matches(".updateReplyBtn")) {
        openUpdateReplyModal(replyId);
    }
});

$replyUpdateBtn.addEventListener("click", () => {
    updateReply($modalReplyId.value);
});