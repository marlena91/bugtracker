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
        console.log(response)
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

function editPriority() {
    const priorityField = document.getElementById("priority-field");
    axios.get(`/issues/priority`).then(response => {
        priorityField.innerHTML = response.data;
    })
        .catch(error => {
            console.log(error);
        })
}
function setPriority() {

    const issueId = document.getElementById("issue-id");
    if (!issueId) {
        return;
    }

    const newPriority = document.getElementById("priority-select").value;
    const body = {
        priority: newPriority
    };
    axios.patch(`/issues/priority/${issueId.value}`, body).then(response => {
        console.log(response.data)

        getHtmlPriorityUpdated(newPriority);
    }).catch(error => {
        console.log(error);
    })
}

function getHtmlPriorityUpdated(newPriority){
    const priorityField = document.getElementById("priority-field");
    axios.get(`/issues/priority-updated`).then(response => {
        priorityField.innerHTML = response.data;
        document.getElementById("priority-value").innerText = newPriority;
    })
        .catch(error => {
            console.log(error);
        })
}

function editType() {
    const typeField = document.getElementById("type-field");
    axios.get(`/issues/type`).then(response => {
        typeField.innerHTML = response.data;
    })
        .catch(error => {
            console.log(error);
        })
}
function setType() {

    const issueId = document.getElementById("issue-id");
    if (!issueId) {
        return;
    }

    const newType = document.getElementById("type-select").value;
    const body = {
        type: newType
    };
    axios.patch(`/issues/type/${issueId.value}`, body).then(response => {
        getHtmlTypeUpdated(newType);
    }).catch(error => {
        console.log(error);
    })
}

function getHtmlTypeUpdated(newType){
    const typeField = document.getElementById("type-field");
    axios.get(`/issues/type-updated`).then(response => {
        typeField.innerHTML = response.data;
        document.getElementById("type-value").innerText = newType;
    })
        .catch(error => {
            console.log(error);
        })
}

