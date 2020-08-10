package com.tuituidan.oss.service;

import com.tuituidan.oss.config.MinioConfig;
import com.tuituidan.oss.exception.ImageHostException;

import io.minio.MinioClient;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * MinioService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/10
 */
@Service
@Slf4j
public class MinioService {

    @Resource
    private MinioConfig minioConfig;

    private MinioClient minioClient;

    @PostConstruct
    private void init() {
        try {
            this.minioClient = new MinioClient(minioConfig.getEndpoint(),
                    minioConfig.getAccessKey(),
                    minioConfig.getSecretKey());
            // 如果该桶不存在，就创建一个，目前需要人为手动配置该桶为只读，后续可通过程序来设置.
            if (!this.minioClient.bucketExists(minioConfig.getBucket())) {
                log.warn("【{}】的桶不存在，将会创建一个。", minioConfig.getBucket());
                this.minioClient.makeBucket(minioConfig.getBucket());
                log.info("【{}】的桶已经创建成功，目前请手动设置该桶为只读。", minioConfig.getBucket());
            }
        } catch (Exception e) {
            throw new ImageHostException("初始化创建 MinioClient 出错，请检查！", e);
        }
    }

}
