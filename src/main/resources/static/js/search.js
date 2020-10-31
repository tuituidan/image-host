/*
 * @author zhujunhan
 * @date 2020/8/1
 */
var pageIndex = 0;
var pageSize = 20;
var loadComplete = false;
var isLoading = false;
var $searchList = $(".card-row");
$(function () {
    $('.modal').modal();
    $('.materialboxed').materialbox();
    $("#search").keypress(function (e) {
        if (e.which === 13) {
            e.preventDefault();
            searchHandler();
        }
    });
    new ClipboardJS('.btnCopy');
    searchHandler();
    //距离底部10像素的时候加载下一页
    scrollBottom(10);
});

//滚动加载
function scrollBottom(bot) {
    $(window).scroll(function () {
        if ((bot + $(window).scrollTop()) >= ($(document).height() - $(
            window).height())) {
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
                $searchList.append(
                    '<div class="search-msg-content">未匹配到任何属于该标签的图片，请重新输入</div>');
                return;
            }
            pageIndex++;
            $.each(result.content, function (index, item) {
                let html = template('itemHtml', item);
                $searchList.append(html);
            });
            $('.materialboxed').materialbox();
            if (loadComplete) {
                $searchList.append(
                    '<div class="search-msg-content">没有更多了</div>');
            }
            isLoading = false;
        }
    );
}

function downloadHandler(cur) {
    window.open("/api/v1/files/" + $(cur).data("id") + "/actions/download");
}

function copyToClipboard(cur) {
    $(".btnCopy").attr("data-clipboard-text", $(cur).data("imgurl")).click();
    utils.showMsg("图片地址已复制到剪贴板中", 1000);
}

function deleteHandler(cur) {
    let delIdCtl = $('#delId');
    if (cur) {
        delIdCtl.text($(cur).data("id"));
        $('#delModal').modal('open');
        return;
    }
    let id = delIdCtl.text();
    utils.ajaxDelete(
        "/api/v1/files/" + id,
        {},
        function () {
            $('#delModal').modal('close');
            $("#" + id).remove();
        }
    );
}

function editHandler(cur) {
    let editIdCtl = $('#editId');
    let editTagsCtl = $('#ctl_tags');
    if (cur) {
        editIdCtl.text($(cur).data("id"));
        $('#editModal').modal('open');
        return;
    }
    let id = editIdCtl.text();
    let tags = editTagsCtl.val();
    utils.ajaxPatch(
        "/api/v1/files/" + editIdCtl.text(),
        {
            tags: tags
        },
        function () {
            $('#editModal').modal('close');
            $("#" + id + " .real-content").text(tags)
        }
    );
}
