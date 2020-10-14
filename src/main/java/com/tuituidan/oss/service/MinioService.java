package com.tuituidan.oss.service;

import com.tuituidan.oss.config.MinioConfig;
import com.tuituidan.oss.consts.Consts;
import com.tuituidan.oss.exception.base.ExceptionBuilder;
import com.tuituidan.oss.kit.FileTypeKit;

import io.minio.*;

import java.io.InputStream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private MinioClient minioClient;

    @PostConstruct
    private void init() {
        try {
            // 如果该桶不存在，就创建一个，目前需要人为手动配置该桶为只读，后续可通过程序来设置.
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build())) {
                log.warn("【{}】的桶不存在，将会创建一个。", minioConfig.getBucket());
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucket()).build());
                log.info("【{}】的桶已经创建成功，目前请手动设置该桶为只读。", minioConfig.getBucket());
            }
        } catch (Exception ex) {
            throw ExceptionBuilder.builder().error("初始化创建 MinioClient 出错，请检查！", ex).build();
        }
    }

    /**
     * 上传文件到 Minio 中.
     *
     * @param objectName  对象名
     * @param inputStream 文件流
     */
    public void putInputStream(String objectName, InputStream inputStream) {
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucket())
                    .contentType(FileTypeKit.getMediaTypeValue(FilenameUtils.getExtension(objectName)))
                    .object(objectName)
                    .stream(inputStream, -1, ObjectWriteArgs.MIN_MULTIPART_SIZE * 4).build());
        } catch (Exception ex) {
            throw ExceptionBuilder.builder().error("向 Minio 中上传文件出错，文件名称-【{}】", objectName, ex).build();
        }
    }

    /**
     * 从minio中删除文件.
     *
     * @param objectName 对象名
     */
    public void deleteObject(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucket()).object(objectName).build());
        } catch (Exception ex) {
            throw ExceptionBuilder.builder().error("从 Minio 中删除文件出错，文件名称-【{}】", objectName, ex).build();
        }
    }

    /**
     * 获取对象的的 URL 链接地址.
     *
     * @param objectName 对象名(包含相对路径)
     * @return URL 地址
     */
    public String getObjectUrl(String objectName) {
        return StringUtils.join(minioConfig.getEndpoint(), Consts.SEP,
                minioConfig.getBucket(), Consts.SEP, objectName);
    }
}
