package com.bigbrother.fileservice.controller;

import com.bigbrother.fileservice.dto.FileInfo;
import com.bigbrother.fileservice.service.LuceneService;
import com.bigbrother.fileservice.service.UploaderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * UploaderController.
 *
 * @author zhujunhan
 */
@RestController
@RequestMapping("/api")
public class UploaderController {

    @Resource
    private UploaderService uploaderService;

    @PostMapping("/v1/upload")
    public ResponseEntity<Map> upload(@RequestParam("file") MultipartFile file,
                                      @RequestBody FileInfo fileInfo) {

        uploaderService.upload(file, fileInfo);
        return ResponseEntity.ok(
                new HashMap<String, Object>(2)
        );
    }

    @Resource
    private LuceneService luceneService;

    @GetMapping("/test/{user}/{tag}")
    public String test(@PathVariable("user") String user,@PathVariable("tag") String tag){
        return String.valueOf(luceneService.create(new FileInfo().setId(UUID.randomUUID().toString()).setUserName(user).setTags(tag)));
    }

    public ResponseEntity<Boolean> update(@RequestBody FileInfo fileInfo){
        return ResponseEntity.ok(true);
    }




    @GetMapping("/list/{search}")
    public ResponseEntity<List> lists(@PathVariable("search") String txt) {
        return ResponseEntity.ok(uploaderService.search(new FileInfo()));
    }
}
