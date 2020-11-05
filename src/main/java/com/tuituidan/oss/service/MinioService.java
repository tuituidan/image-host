package com.tuituidan.oss.service;

import com.tuituidan.oss.config.MinioConfig;
import com.tuituidan.oss.consts.Separator;
import com.tuituidan.oss.exception.ImageHostException;
import com.tuituidan.oss.util.FileTypeUtils;
import com.tuituidan.oss.util.HashMapUtils;
import com.tuituidan.oss.util.StringExtUtils;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectTagsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteArgs;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.SetObjectTagsArgs;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
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

    /**
     * 配置桶只读的是个比较长的json字符，放在配置文件中.
     */
    private static final String MINIO_CONFIG = "config/minio-policy.json";

    @PostConstruct
    private void init() {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build())) {
                return;
            }
            log.warn("【{}】的桶不存在，将会创建一个。", minioConfig.getBucket());
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucket()).build());

            // 设置桶为只读权限
            String policy = StringExtUtils.streamToString(new ClassPathResource(MINIO_CONFIG).getInputStream());
            policy = policy.replace("bucket-name", minioConfig.getBucket());
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .config(policy)
                    .build());
            log.info("【{}】的桶已经创建成功", minioConfig.getBucket());
        } catch (Exception ex) {
            throw new ImageHostException("初始化创建 MinioClient 出错，请检查！", ex);
        }
    }

    /**
     * 上传文件到 Minio 中.
     *
     * @param objectName 对象名
     * @param inputStream 文件流
     */
    public void putObject(String objectName, Map<String, String> tags, InputStream inputStream) {
        try {
            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucket())
                    .contentType(FileTypeUtils.getMediaTypeValue(FilenameUtils.getExtension(objectName)))
                    .object(objectName)
                    .tags(tags)
                    .stream(inputStream, -1, ObjectWriteArgs.MIN_MULTIPART_SIZE * 4L).build());
        } catch (Exception ex) {
            throw ImageHostException.builder().tip("文书上传失败")
                    .error("向 Minio 中上传文件出错，文件名称-【{}】", objectName, ex).build();
        }
    }

    /**
     * 修改标签.
     *
     * @param objectName objectName
     * @param tag tag
     */
    public void updateTags(String objectName, String tag) {
        try {
            Map<String, String> tags = minioClient.getObjectTags(GetObjectTagsArgs.builder()
                    .bucket(minioConfig.getBucket())
                    .object(objectName)
                    .build()).get();
            if (null == tags) {
                tags = HashMapUtils.newFixQuarterSize();
            } else {
                tags = new HashMap<>(tags);
            }
            tags.put("info", tag);
            minioClient.setObjectTags(SetObjectTagsArgs.builder().bucket(minioConfig.getBucket())
                    .object(objectName)
                    .tags(tags)
                    .build());
        } catch (Exception ex) {
            throw ImageHostException.builder().tip("标签修改失败")
                    .error("修改标签失败，文件名称-【{}】，标签-【{}】", objectName, tag, ex).build();
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
            throw ImageHostException.builder().tip("文件删除失败")
                    .error("从 Minio 中删除文件出错，文件名称-【{}】", objectName, ex).build();
        }
    }

    /**
     * getObject.
     *
     * @param objectName objectName
     * @return InputStream
     */
    public InputStream getObject(String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(minioConfig.getBucket()).object(objectName).build());
        } catch (Exception ex) {
            throw ImageHostException.builder().tip("文件获取失败")
                    .error("从 Minio 中获取文件出错，文件名称-【{}】", objectName, ex).build();
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
