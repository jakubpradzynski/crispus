$(document).ready( function() {
    // $(window).scroll(function() {
    //     if ($(this).scrollTop() > 50) {
    //         $('#menu').css({
    //             'display': 'none'
    //         });
    //     }
    // });
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