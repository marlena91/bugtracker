function editStatus() {
    const statusField = document.getElementById("status-field");
    axios.get(`/issues/status`).then(response => {
        statusField.innerHTML = response.data;
    })
        .catch(error => {
            console.log(error);
        })
}
function setStatus() {

    const issueId = document.getElementById("issue-id");
    if (!issueId) {
        return;
    }

    const newStatus = document.getElementById("status-select").value;
    const body = {
        status: newStatus
    };
    axios.patch(`/issues/status/${issueId.value}`, body).then(response => {
        console.log(response);
        getHtmlStatusUpdated(newStatus);
    }).catch(error => {
        console.log(error);
    })
}

function getHtmlStatusUpdated(newStatus){
    const statusField = document.getElementById("status-field");
    axios.get(`/issues/status-updated`).then(response => {
        statusField.innerHTML = response.data;
        document.getElementById("status-value").innerText = newStatus;
    })
        .catch(error => {
            console.log(error);
        })
}

