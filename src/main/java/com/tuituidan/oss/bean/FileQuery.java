package com.tuituidan.oss.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * FileQuery.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Getter
@Setter
public class FileQuery {

    /**
     * tags.
     */
    private String tags;

    /**
     * 分页查询的页数.
     */
    private Integer pageIndex;

    /**
     * 分页查询的页大小.
     */
    private Integer pageSize;

}
