/*  index.html */
$(document).ready(() => {
    $("#logoutLink").on("click",(e) => {
        e.preventDefault();
        document.logoutForm.submit();
    })

    /* navigation.html */
    customizeDropDownMenu();

});

const customizeDropDownMenu = () => {
    // click the a tag in the class dropdown
    $(".dropdown > a").click(function () {
        location.href = this.href;
    })
}