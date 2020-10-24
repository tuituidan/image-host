var upload;

(function ($) {
    upload = $('#uploader').createUploader({
        serverUrl: "/api/v1/files/actions/upload",
        // 允许上传的文件格式
        acceptExt: "jpg,jpeg,png,mp4"
    });
    new ClipboardJS('.btnCopy');
})(jQuery);

function uploadHandler() {
    upload.uploadFile();
}
