package com.bigbrother.fileservice.controller;

import com.bigbrother.fileservice.dto.FileInfo;
import com.bigbrother.fileservice.service.LuceneService;
import com.bigbrother.fileservice.service.UploaderService;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<FileInfo> upload(@Valid @RequestBody FileInfo fileInfo) {
        fileInfo.setId(UUID.randomUUID().toString());
        uploaderService.upload(fileInfo);
        return ResponseEntity.ok(fileInfo);
    }

    @Resource
    private LuceneService luceneService;

    @GetMapping("/test/{user}/{tag}")
    public String test(@PathVariable("user") String user, @PathVariable("tag") String tag) {
        String id = UUID.randomUUID().toString();
        return String.valueOf(luceneService.create(new FileInfo().setId(id)
                .setUserName(user).setTags(tag)));
    }


    public ResponseEntity<Boolean> update(@RequestBody FileInfo fileInfo) {
        return ResponseEntity.ok(true);
    }

    /**
     * 修补
     * @return
     */
    public ResponseEntity<Boolean> patch(String tags,String objectName){
        return ResponseEntity.ok(true);
    }

    @GetMapping("/list/{search}")
    public ResponseEntity<List> lists(@PathVariable("search") String txt) {
        return ResponseEntity.ok(uploaderService.search(new FileInfo()));
    }
}
