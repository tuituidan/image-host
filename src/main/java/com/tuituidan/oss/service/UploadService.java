package com.tuituidan.oss.service;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UploadService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Slf4j
@Service
public class UploadService {
    /**
     * minioService.
     */
    @Resource
    private MinioService minioService;
}
