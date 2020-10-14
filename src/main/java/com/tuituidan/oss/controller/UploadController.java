package com.tuituidan.oss.controller;

import com.tuituidan.oss.bean.FileInfo;
import com.tuituidan.oss.bean.UploadResult;
import com.tuituidan.oss.consts.Consts;
import com.tuituidan.oss.service.UploadService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UploadController.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Api(tags = "上传文件接口")
@RestController
@RequestMapping(Consts.API_V1 + "/files")
public class UploadController {

    @Resource
    private UploadService uploadService;

    /**
     * webuploader上传文件的接口.
     *
     * @param fileInfo fileInfo
     * @return UploadResult
     */
    @ApiOperation("webuploader上传文件的接口")
    @ApiImplicitParam(name = "file", value = "上传的文件", required = true, dataType = "file")
    @PostMapping("/actions/upload")
    public ResponseEntity<UploadResult> upload(@Valid FileInfo fileInfo) {
        Assert.isTrue(!fileInfo.getFile().isEmpty(), "上传文件不能为空！");
        return ResponseEntity.ok(UploadResult.builder().id(fileInfo.getId())
                .url(uploadService.upload(fileInfo)).build());
    }

    /**
     * base64上传文件的接口.
     *
     * @param inputStream inputStream
     * @return String
     */
    @ApiOperation("base64上传文件的接口")
    @ApiImplicitParam(name = "inputStream", value = "base64编码据", required = true)
    @PostMapping("/base64/actions/upload")
    public ResponseEntity<String> base64(ServletInputStream inputStream) {
        return ResponseEntity.ok(uploadService.uploadBase64(inputStream));
    }
}
