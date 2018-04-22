$(document).ready( function() {
    $(function () {
        $(window).scroll(function () {
            var scroll = $(window).scrollTop();
            var done = false;
            if (scroll >= 50 && done === false) {
                $('#menu').hide(500);
            } else {
                $('#menu').show(500);
            }
        })
    });
});