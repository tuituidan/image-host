package com.tuituidan.oss.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * FileDoc.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Getter
@Setter
@Document(indexName = "image-host", type = "file")
public class FileDoc implements Serializable {
    private static final long serialVersionUID = -3633968171072796648L;
    /**
     * id.
     */
    @Id
    private String id;

    /**
     * 文件名.
     */
    @Field(type = FieldType.Keyword, store = true)
    private String name;

    /**
     * 文件标签.
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String tags;

    /**
     * 创建时间.
     */
    @Field(type = FieldType.Date)
    private LocalDateTime createDate;

    /**
     * 文件存储路径.
     */
    @Field(type = FieldType.Keyword, index = false)
    private String path;

    /**
     * md5值.
     */
    @Field(type = FieldType.Keyword, index = false)
    private String md5;
}
