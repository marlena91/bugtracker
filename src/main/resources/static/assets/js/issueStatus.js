function setStatus() {
    const issueId = document.getElementById("issue-id");
    if (!issueId) {
        return;
    }

    const newStatus = document.getElementById("status-select").value;
    console.log(newStatus);

    const body = {
        status: newStatus
    };

    axios.patch(`/issues/status/${issueId.value}`, body).then(response => {
        console.log(response);
    }).catch(error => {
        console.log(error);
    })
}