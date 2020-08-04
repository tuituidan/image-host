package com.tuituidan.oss.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * IndexController.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/4
 */
@Api(tags = "首页")
@RestController
@RequestMapping("/api/v1")
public class IndexController {

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


}
