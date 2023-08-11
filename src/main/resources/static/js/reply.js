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
    const url = `/api/blogs/${blogId}/replies`;

    // Call the httpRequest method instead of fetch
    httpRequest('GET', url, null,
        (replies) => {
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
                <div class="card-text" data-replyId="${reply.replyId}">${reply.replyContent}</div>
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
        },
        (error) => {
            console.error("Error while getting all replies:", error);
        }
    );
}

function deleteReply(replyId) {
    if (confirm("Are you sure you want to delete this reply?")) {
        const url = `/api/replies/${replyId}`;

        httpRequest('DELETE', url, null,
            () => {
                // const replyElement = document.getElementById(`replyId${replyId}`);
                // if (replyElement) {
                //     replyElement.remove();
                // }
                getAllReplies(blogId); // UI update
            },
            (error) => {
                console.error("Error while deleting the reply:", error);
            }
        );
    }
}

function addReply() {
    const replyContent = document.getElementById("replyContent").value.trim();
    const url = '/api/replies';

    if (replyContent === "") {
        alert("Please fill Content fields.");
        return;
    }

    // Use the httpRequest function instead of fetch
    httpRequest('POST', url,
        JSON.stringify({
            replyContent,
            blogId,
        }),
        (data) => {
            document.getElementById("replyContent").value = "";
            getAllReplies(blogId); // UI update
        },
        (error) => {
            console.error("Error while inserting the reply:", error);
        }
    );
}

function openUpdateReplyModal(replyId) {
    const url = `/api/replies/${replyId}`;

    httpRequest('GET', url, null,
        (data) => {
            // Data received from the server
            // Populate the reply content inside the modal input field
            const $modalReplyContent = document.getElementById("modalReplyContent");
            $modalReplyContent.value = data.replyContent;

            // Store the replyId in a hidden input field for later use during the update
            const $modalReplyId = document.getElementById("modalReplyId");
            $modalReplyId.value = data.replyId;
        },
        (error) => {
            console.error('Error while fetching reply data:', error);
        }
    );
}

function updateReply(replyId) {
    const url = `/api/replies/${replyId}`;

    console.log($modalReplyContent.value);

    httpRequest('PUT', url,
        JSON.stringify({
            replyContent: $modalReplyContent.value
        }),
        () => {
            $modalReplyContent.value = "";
            getAllReplies(blogId);
        },
        (error) => {
            console.error("Error while updating the reply :", error);
        }
    );
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