/*
 * @author zhujunhan
 * @date 2020/8/1
 */
var pageIndex = 0;
var pageSize = 5;
var loadComplete = false;
var isLoading = false;
var $searchList = $(".card-row");
$(function () {
    $('.materialboxed').materialbox();
    $("#search").keypress(function (e) {
        if (e.which === 13) {
            e.preventDefault();
            searchHandler();
        }
    });
    searchHandler();
    //距离底部10像素的时候加载下一页
    scrollBottom(10);
});

//滚动加载
function scrollBottom(bot) {
    $(window).scroll(function () {
        if ((bot + $(window).scrollTop()) >= ($(document).height() - $(window).height())) {
            if (!loadComplete) {
                loadList();
            }
        }
    });
}
function searchHandler() {
    pageIndex = 0;
    loadComplete = false;
    isLoading = false;
    $searchList.html('');
    loadList();
}


function loadList() {
    if (isLoading) {
        return;
    }
    isLoading = true;
    utils.ajaxGet(
        "/api/v1/files/",
        {
            tags: $('#search').val(),
            pageIndex: pageIndex,
            pageSize: pageSize
        },
        function (result) {
            loadComplete = result.last;
            if (result.totalElements <= 0) {
                $searchList.append('<div class="search-msg-content">未匹配到任何属于该标签的图片，请重新输入</div>');
                return;
            }
            pageIndex++;
            $.each(result.content, function (index, item) {
                var html = template('itemHtml', item);
                $searchList.append(html);
            });
            $('.materialboxed').materialbox();
            if (loadComplete) {
                $searchList.append('<div class="search-msg-content">没有更多了</div>');
            }
            isLoading = false;
        }
    );
}
