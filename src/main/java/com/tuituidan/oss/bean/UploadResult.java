package com.tuituidan.oss.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * UploadResult.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Setter
@Getter
@Builder
public class UploadResult {

    /**
     * 返回给前端的上传文件的可访问URL.
     */
    private String url;

    /**
     * 文件上传id，返回给前端可用于删除.
     */
    private String id;
}
