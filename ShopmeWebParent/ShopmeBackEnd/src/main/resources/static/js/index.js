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

    $(".navbar .dropdown").hover(
        function () {
            $(this).find('.dropdown-menu')
                .first()
                .stop(true, true)
                .delay(100)
                .slideDown();
        },
        function () {
            $(this).find('.dropdown-menu')
                .first()
                .stop(true, true)
                .delay(50)
                .slideUp();
        }
    );

    // click the a tag in the class dropdown
    $(".dropdown > a").click(function () {
        location.href = this.href;
    })
}