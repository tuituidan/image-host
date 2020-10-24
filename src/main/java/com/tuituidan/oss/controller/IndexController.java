package com.tuituidan.oss.controller;

import com.tuituidan.oss.bean.FileDoc;
import com.tuituidan.oss.bean.FileQuery;
import com.tuituidan.oss.consts.Consts;
import com.tuituidan.oss.service.ElasticsearchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * IndexController.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/4
 */
@Api(tags = "首页")
@RestController
@RequestMapping(Consts.API_V1)
public class IndexController {

    @Resource
    private ElasticsearchService elasticsearchService;

    /**
     * say hello.
     *
     * @return String
     */
    @ApiOperation("hello")
    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("hello");
    }

    /**
     * 分页查询接口.
     *
     * @param fileQuery fileQuery
     * @return Page
     */
    @ApiOperation("分页查询接口")
    @GetMapping("/files")
    public ResponseEntity<Page<FileDoc>> search(FileQuery fileQuery) {
        return ResponseEntity.ok(elasticsearchService.search(fileQuery));
    }

    /**
     * 删除接口.
     *
     * @param id 文件id
     * @return Boolean
     */
    @ApiOperation("删除接口")
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        elasticsearchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 修改标签接口.
     *
     * @param id 文件id
     * @return Boolean
     */
    @ApiOperation("修改标签接口")
    @PatchMapping("/files/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestParam("tag") String tag) {
        elasticsearchService.update(id, tag);
        return ResponseEntity.noContent().build();
    }
}
