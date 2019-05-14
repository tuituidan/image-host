package com.bigbrother.fileservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 */
@Getter
@Setter
@Accessors(chain = true)
public class FileInfo {

    /**
     * 文件ID（文件的md5值）由前端生成
     */
    @NotBlank(message = "文件ID不能为空，请传入文件的MD5值")
    @Pattern(regexp = "^([a-fA-F0-9]{32})$",message = "id必须是文件的MD5值")
    private String id;

    /**
     * 用户名，文件所属用户
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 文件存储路径，用于结果返回，索引中作为ID不分词全匹配
     */
    private String fileName;

    /**
     * 文件标签，用于搜索，索引中分词匹配
     */
    @NotBlank(message = "文件标签不能为空")
    private String tags;

    /**
     * 分片上传时的当前分片数
     */
    private Integer chunk;

    /**
     * 分片的大小，注意不是当前分片的大小
     */
    @NotNull(message = "请传入分片的大小，注意不是当前分片的大小")
    private Integer chunkSize;

    /**
     * 分片上传时的分片总数
     */
    private Integer chunks;

    /**
     * 文件
     */
    @NotNull(message = "上传文件不能为空")
    private MultipartFile file;
}
