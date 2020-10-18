var upload;

(function ($) {
    upload = $('#uploader').createUploader();
})(jQuery);
function uploadHandler() {
    upload.uploadFile(function (pics) {

    });
}
