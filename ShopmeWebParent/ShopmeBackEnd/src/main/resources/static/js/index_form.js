$(document).ready(() => {

    $("#buttonCancel").on("click",() => {
        window.location = moduleURL;
    });

    // show image on form
    $("#fileImage").change(function () {
        let fileSize = this.files[0].size;

        if(fileSize > 102400){
            this.setCustomValidity("You must choose an image less than 100KB");
            // show setCustomValidity
            this.reportValidity();
        }else {
            showImageThumbnail(this);
        }

    });

});


// get object from input file
const showImageThumbnail = (fileInput) => {

    // array
    let file = fileInput.files[0];

    let reader = new FileReader();
    // onload: after it has been successfully loaded
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };

    // read binary data and encode it as base64 data url
    reader.readAsDataURL(file);
}

/* dialog */
const showModalDialog =(title, message) =>{
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    $("#modalDialog").modal();
}

const showErrorModal = (message) => {
    showModalDialog("Error", message);
}

const showWarningModal = (message) => {
    showModalDialog("Warning", message);
}