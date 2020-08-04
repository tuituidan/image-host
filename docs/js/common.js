/**
 * 封装一些公用的js
 **/

(function ($) {

    function Message(msg) {
        this.msg = msg;//显示内容
        this.init();
    }

    Message.prototype = {
        init: function () {
            var _this = this;
            var $body = $("body");
            var $div = $("<div class='toast'>" + _this.msg + "</div>");
            $body.append($div);
            $div.css("left", $body.width() / 2 - $div.width() / 2);
            $div.css("top", $(window).height() / 2);
            _this.currentMsg = $div;
        },
        show: function (time) {
            var duration = time || 3000;
            var _this = this;
            _this.currentMsg.fadeIn(300);

            setTimeout(function () {
                _this.currentMsg.fadeOut(300);
            }, duration);
        }
    };

    window.utils = {
        showMsg: function (msg, duration) {
            new Message(msg).show(duration || 3000);
        },
        /**
         * 封装ajax-get请求，不用$.get是想关闭缓存和统一处理error
         * @param url
         * @param data
         * @param success
         */
        ajaxGet: function (url, data, success) {
            $.ajax({
                url: url,
                type: "get",
                data: data,
                cache: false,
                success: success,
                error: function (xhr, status, error) {
                    utils.showMsg(xhr.responseText);
                }
            });
        },
        ajaxDelete: function (url, data, success) {
            $.ajax({
                type: "delete",
                url: url,
                cache: false,
                success: success,
                error: function (xhr, status, error) {
                    utils.showMsg(xhr.responseText);
                }
            });
        }
    };

})(jQuery);
