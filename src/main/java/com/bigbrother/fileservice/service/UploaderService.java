package com.bigbrother.fileservice.service;

import com.bigbrother.fileservice.dto.FileInfo;
import com.bigbrother.fileservice.exception.FileServiceRuntimeException;
import com.bigbrother.fileservice.utils.SysUtil;

import io.minio.MinioClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class UploaderService {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    /**
     * minioClient.
     */
    private MinioClient minioClient;

    @Resource
    private LuceneService luceneService;

    /**
     * Bean创建后初始化 MinioClient 实例，并检查是否存在配置的桶，如果不存在就创建一个.
     */
    @PostConstruct
    public void init() {
        /*try {
            this.minioClient = new MinioClient(endpoint, accessKey, secretKey);
            if (!this.minioClient.bucketExists(this.bucket)) {
                this.minioClient.makeBucket(this.bucket);
            }
        } catch (Exception e) {
            log.error("初始化创建 MinioClient 出错，请检查！", e);
        }*/
    }

    /**
     * @param fileInfo 上传文件信息
     */
    public boolean upload(FileInfo fileInfo) {
        String fileName = saveToMinio(fileInfo);
        if (StringUtils.isBlank(fileName)) {
            return false;
        }
        fileInfo.setFileName(fileName);
        return luceneService.create(fileInfo);
    }

    private String saveToMinio(FileInfo fileInfo) {
        String objectName = SysUtil.getObjectName(fileInfo);
        try {
            if (null == fileInfo.getChunks()) {
                this.minioClient.putObject(this.bucket, objectName, fileInfo.getFile().getInputStream(),
                        MediaType.APPLICATION_OCTET_STREAM_VALUE);
                return objectName;
            } else {
                File tempFile = getTempFileName(fileInfo);
                if (null != tempFile) {
                    //所有分片上传完成后才会返回fileName
                    this.minioClient.putObject(this.bucket, objectName, tempFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            throw new FileServiceRuntimeException("上传的文件转换为服务器系统文件出错", e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 合并分片文件，合并完成删除文件
     *
     * @param fileInfo
     * @return File
     * @throws IOException
     */
    private File getTempFileName(FileInfo fileInfo) throws IOException {
        String fileName = SysUtil.getTempFileFolder().concat(fileInfo.getFile().getOriginalFilename());
        File tempFile = new File(fileName);
        if (!tempFile.getParentFile().exists() && !tempFile.getParentFile().mkdirs()) {
            throw new FileServiceRuntimeException("文件夹创建失败");
        }
        fileInfo.getFile().transferTo(tempFile);
        return tempFile;
    }

    public boolean update(FileInfo fileInfo) {
        return luceneService.update(fileInfo);
    }

    public boolean delete(FileInfo fileInfo) {
        try {
            minioClient.removeObject(bucket, SysUtil.getObjectName(fileInfo));
            return luceneService.delete(fileInfo);
        } catch (Exception e) {
            throw new FileServiceRuntimeException("删除失败", e);
        }
    }

    public List<FileInfo> search(FileInfo fileInfo) {
        return luceneService.search(fileInfo);
    }


}
