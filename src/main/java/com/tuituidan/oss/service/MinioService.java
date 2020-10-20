package com.tuituidan.oss.service;

import com.tuituidan.oss.config.MinioConfig;
import com.tuituidan.oss.consts.Separator;
import com.tuituidan.oss.exception.ImageHostException;
import com.tuituidan.oss.kit.FileTypeKit;

import io.minio.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
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
        try (InputStream inputStream = new ClassPathResource("config/minio-policy.json").getInputStream()) {
            // 如果该桶不存在，就创建一个
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build())) {
                log.warn("【{}】的桶不存在，将会创建一个。", minioConfig.getBucket());
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucket()).build());

                // 设置桶为只读权限
                String policy = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                policy = policy.replace("bucket-name", minioConfig.getBucket());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(minioConfig.getBucket())
                        .config(policy)
                        .build());
                log.info("【{}】的桶已经创建成功", minioConfig.getBucket());
            }
        } catch (Exception ex) {
            throw ImageHostException.builder().error("初始化创建 MinioClient 出错，请检查！", ex).build();
        }
    }

    /**
     * 上传文件到 Minio 中.
     *
     * @param objectName  对象名
     * @param inputStream 文件流
     */
    public void putObject(String objectName, Map<String, String> tags, InputStream inputStream) {
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucket())
                    .contentType(FileTypeKit.getMediaTypeValue(FilenameUtils.getExtension(objectName)))
                    .object(objectName)
                    .tags(tags)
                    .stream(inputStream, -1, ObjectWriteArgs.MIN_MULTIPART_SIZE * 4L).build());
        } catch (Exception ex) {
            throw ImageHostException.builder().error("向 Minio 中上传文件出错，文件名称-【{}】", objectName, ex).build();
        }
    }

    /**
     * 从minio中删除文件.
     *
     * @param objectName 对象名
     */
    public void deleteObject(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucket()).object(objectName).build());
        } catch (Exception ex) {
            throw ImageHostException.builder().error("从 Minio 中删除文件出错，文件名称-【{}】", objectName, ex).build();
        }
    }

    /**
     * 获取对象的的 URL 链接地址.
     *
     * @param objectName 对象名(包含相对路径)
     * @return URL 地址
     */
    public String getObjectUrl(String objectName) {
        return StringUtils.join(minioConfig.getEndpoint(), Separator.SLASH,
                minioConfig.getBucket(), Separator.SLASH, objectName);
    }
}
