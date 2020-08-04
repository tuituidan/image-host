/*
 * @author zhujunhan
 * @date 2020/8/1
 */
(function ($) {
    $(function () {
        const $searchList = $(".card-row");
        for (let i = 1; i < 19; i++) {
            $searchList.append(template('itemHtml', {index: i}));
        }
        $('.materialboxed').materialbox();
    });
})(jQuery);
