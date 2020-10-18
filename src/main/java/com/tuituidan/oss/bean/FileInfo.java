package com.tuituidan.oss.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileInfo.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Getter
@Setter
@Accessors(chain = true)
public class FileInfo {

    /**
     * 文件ID.
     */
    @NotBlank(message = "文件ID不能为空")
    private String id;

    /**
     * 文件名.
     */
    @NotBlank(message = "文件名不能为空")
    private String name;

    /**
     * 文件扩展名，不含点（{@code .}）号.
     *
     * @since v2.2.0
     */
    private String ext;

    /**
     * 文件标签，用于搜索，索引中分词匹配.
     */
    private String tags;

    /**
     * 文件.
     */
    @NotNull(message = "上传文件不能为空")
    private MultipartFile file;

    /**
     * base64.
     */
    private String base64;

    /**
     * md5.
     */
    private String md5;

    /**
     * 是否压缩图片.
     */
    private boolean compress;

    /**
     * 分片上传时的当前分片数.
     */
    private Integer chunk;

    /**
     * 分片的大小，注意不是当前分片的大小.
     */
    @NotNull(message = "请传入分片的大小，不是注意不是当前分片的大小")
    private Integer chunkSize;

    /**
     * 分片上传时的分片总数.
     */
    private Integer chunks;
}
