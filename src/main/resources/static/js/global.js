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

    $(function () {
        var now = new Date();
        var month = (now.getMonth() + 1);
        var day = now.getDate();
        if (month < 10)
            month = "0" + month;
        if (day < 10)
            day = "0" + day;
        var today = now.getFullYear() + '-' + month + '-' + day;
        $('#datePicker').val(today);
    });

});