$(document).ready(() => {

    $("#buttonCancel").on("click",() => {
        window.location = moduleURL;
    });

    // show image on form
    $("#fileImage").change(function () {
        let fileSize = this.files[0].size;

        if(fileSize > 1048576){
            this.setCustomValidity("You must choose an image less than 1MB");
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