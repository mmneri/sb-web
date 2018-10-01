
$(document).ready(function () {
    $.ajax({
        url: 'http://localhost:8181/fff/players',
        type: 'GET',
        dataType: 'json',
        success: function (result) {
            var insert = '';
            $.each(result, function (index, item) {
                insert += '<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12"><div class="course-item"><div class="course-img"><img src="' + item.photoURL + '" alt="" width="100%"></div><div class="course-body"><div class="course-desc"><h4 class="course-title"><a href="#">' + item.nom + '</a></h4></div></div><div class="course-footer"><div class="course-seats">&nbsp;</div><div class="course-button"><a href="#">VOIR PLUS</a></div></div></div></div>';
            });
            $('#players').html(insert);
        }
    });
});
