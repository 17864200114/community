function likeComment(e) {
    //获得点赞人id（当前用户）
    var likerId = $("#user_id").val();
    console.log(likerId);
    if(likerId==null){
        var isAccepted = confirm("请您登录后再进行次操作。");
        if(isAccepted){
            debugger;
            var questionId = $("#question_id").val();
            window.localStorage.setItem("question_id",questionId);
            window.location.href="http://localhost:8080/login";
        }
        return;
    }

    //点赞表：点赞人id，被点赞comment id，操作（点赞或取消点赞）
    //获得commentid（被点赞comment的id）
    var commentId = e.getAttribute("data-id");
    console.log(commentId);

    var likeType = e.getAttribute("likeType");
    $.ajax({
        type:"POST",
        url:"/commentLike",
        contentType:'application/json',
        data:JSON.stringify({
            "commentId":commentId,
            "likerId":likerId,
            "type":likeType
        }),
        success: function (response) {
            if(response.code == 200){
                location.reload();
            }else {
                    alert(response.message);
            }
            console.log(response);
        },
        dataType:"json"
    });
}

//对主题提交回复
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId,1,content);
}


//对一级评论进行评论
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2target(commentId,2,content);
}

function comment2target(targetId,type,content) {
    if(!content){
        alert("您的回复内容为空")
    }
    //通过method = RequestMethod.POST向Controller发送评论信息
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:'application/json',
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if(response.code == 200){
                location.reload();
            }else {
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        debugger;
                        var questionId = $("#question_id").val();
                        window.localStorage.setItem("question_id",questionId);
                        window.location.href="http://localhost:8080/login";
                    }
                }else{
                    alert(response.message);
                }
            }
            console.log(response);
        },
        dataType:"json"
    });

}


//二级评论(展开或折叠）
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    //获取二级评论展开状态
    var collapse = e.getAttribute("data-collapse");
    if(collapse){
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("icon-color-active");
    }else {
        var subCommentContainer = $("#comment-"+id);
        if(subCommentContainer.children().length != 1){
            //展开二级评论
            comments.addClass("in");
            //标记二级评论状态
            e.setAttribute("data-collapse", 'in');
            e.classList.add("icon-color-active");
        }else {
            //通过method = RequestMethod.GET获得二级评论
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeftElement = $("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>",{
                        "class":"media-object img-rounded",
                        "src":comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h4/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD HH:mm')
                    })));

                    var mediaElement = $("<div/>",{
                        "class":"media"
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement= $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });

                //展开二级评论
                comments.addClass("in");
                //标记二级评论状态
                e.setAttribute("data-collapse", 'in');
                e.classList.add("icon-color-active");
            });
        }
    }
}





function infoEdit() {
    document.getElementById("bio").readOnly=false;
    document.getElementById("password").readOnly=false;
    document.getElementById("avatarUrl").readOnly=false;
}

// $(function(){
//     console.log("已执行");
//     $(".hover-color").hover(
//         function () {
//             $(".hover-color").css("color","#499ef3");
//             console.log("已执行");
//         },
//         function () {
//             $(".hover-color").css("color","#999");
//         }
//     );
// });
