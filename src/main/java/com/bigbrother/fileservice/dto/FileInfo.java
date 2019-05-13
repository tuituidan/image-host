package com.bigbrother.fileservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Getter
@Setter
@Accessors(chain = true)
public class FileInfo {
    private String userName;
    private String fileName;
    private String tags;
    private Integer chunk;
    private Integer chunks;
}
