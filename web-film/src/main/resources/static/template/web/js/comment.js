function loadComment(filmId, page) {
    var url = "/ajax/comment/list?filmId=" + filmId + "&page=" + page;
    $.get(url, function (data) {
        $("#comment_list").replaceWith($(data));
    });
}

function loadSubComment(id) {
    var page = $('#sub-page_' + id).val();
    var filmId = $('#ip-filmId').val();
    $.get("/ajax/subComment/list?commentId=" + id + "&filmId=" + filmId + "&page=" + page, function (data) {
        var scm = ".sub-comment_" + id;
        $(scm).replaceWith($(data));
    });
}

function loadSubComment2(id, page) {
    var filmId = $('#ip-filmId').val();
    $.get("/ajax/subComment/list?commentId=" + id + "&filmId=" + filmId + "&page=" + page, function (data) {
        var scm = ".sub-comment-list_" + id;
        $(scm).replaceWith($(data));
    });
}

$('#form-comment').submit(function (e) {
    e.preventDefault();
    var filmId = $('#filmId').val();
    var formData = $('#form-comment').serializeArray();
    var data = convertToJson(formData);
    var url = "/api/film/comment";
    commentApi(data, url, "post", undefined);
});

function commentApi(data, url, method, action) {
    $.ajax({
        url: url,
        type: method,
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function (id) {
            if ($('#reply-cm-id').val() != "") {
                loadNewSubComment($('#reply-cm-id').val(), id);
            } else {
                loadNewComment(id);
            }
            $('#comment-content-txt').val("");
        },
        error: function (error) {
            action();
        }
    });
}

function loadNewComment(id) {
    $.get("/ajax/comment/new?id=" + id, function (data) {
        $("#new-comment").replaceWith($(data));
    });
}

function loadNewSubComment(cmid, scmid) {
    $.get("/ajax/sub-comment/new?id=" + scmid, function (data) {
        $("#new-sub-cm_" + cmid).replaceWith($(data));
    });
}


function showReplyCm(id) {
    var cmo = $('#cm_' + id);
    var cm = $('#cm-reply');
    cm.find("h6").find("a").text(cmo.find("h6").text());
    cm.find("span").text(cmo.find("span").text());
    cm.find("#reply-content").text(cmo.find(".cm-content").text());
    $('#reply-cm-id').attr({
        value: id,
        name: 'comment.id'
    });
    cm.removeClass("hide");
}

$('#cm-reply .close a').click(function () {
    var cm = $('#cm-reply');
    cm.addClass("hide");
    $('#reply-cm-id').attr({
        name: '',
        value: ''
    });
});

function warningBeforeDelete(x) {
    if (x != null) {
        showAlertBeforeDelete(function () {
            deleteComment([x], function () {
                $('#cm_' + x).remove();
                $('.scm_' + x).remove();
                $('#new-sub-cm_' + x).remove();
                $('.sub-comment_' + x).remove();
            });
        });
    } else {
        showAlertBeforeDelete(function () {
            var ids = $('tbody input[type=checkbox]:checked').map(function () {
                return $(this).val();
            }).get();
            deleteComment(ids);
        });
    }
}

var apiComment = "/api/film/comment";

function deleteComment(data, action) {
    $.ajax({
        url: apiComment,
        type: 'DELETE',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (result) {
            action();
        },
        error: function (result) {
        }
    });
}

var apiCommentLike = "/api/film/comment/like";

function likeComment(comment_id) {
    var name = "input[name='comment_like_" + comment_id + "']";
    var like = $(name).val();
    var data = {};
    if (like == "1") {
        data["status"] = 0;
        $(name).val(0);
        $("#like-cm_" + comment_id + " i").removeClass("fas");
        $("#like-cm_" + comment_id + " i").addClass("far");
    } else {
        data["status"] = 1;
        $(name).val(1);
        $("#like-cm_" + comment_id + " i").removeClass("far");
        $("#like-cm_" + comment_id + " i").addClass("fas");
    }
    data["comment"] = {id: comment_id};
    $.ajax({
        url: apiCommentLike,
        type: 'POST',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (totalLike) {
            $('#totalLike' + comment_id).text(totalLike);
        },
        error: function (result) {
        }
    });
}