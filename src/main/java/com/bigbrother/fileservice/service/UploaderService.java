package com.bigbrother.fileservice.service;

import com.bigbrother.fileservice.consts.SysConst;
import com.bigbrother.fileservice.dto.FileInfo;
import com.bigbrother.fileservice.exception.FileServiceRuntimeException;

import io.minio.MinioClient;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

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
        try {
            this.minioClient = new MinioClient(endpoint, accessKey, secretKey);
            if (!this.minioClient.bucketExists(this.bucket)) {
                this.minioClient.makeBucket(this.bucket);
            }
        } catch (Exception e) {
            log.error("初始化创建 MinioClient 出错，请检查！", e);
        }
    }

    /**
     * 上传前端上传的文件到本地临时文件中，然后才上传到 Minio 中.
     *
     * @param file 上传的文件
     */
    public boolean upload(MultipartFile file, FileInfo fileInfo) {
        fileInfo.setFileName(StringUtils.join(UUID.randomUUID().toString(), SysConst.SEP_DOT,
                FilenameUtils.getExtension(file.getOriginalFilename())));
        saveToMinio(file, fileInfo);
        return luceneService.create(fileInfo);
    }

    private void saveToMinio(MultipartFile file, FileInfo fileInfo) {
        String objectName = getObjectName(fileInfo);
        try {
            if (null == fileInfo.getChunks()) {
                this.minioClient.putObject(this.bucket, objectName, file.getInputStream(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
            } else {
                File tempFile = getTempFileName(file);
                if (null != tempFile) {
                    //所有分片上传完成后才会返回fileName
                    this.minioClient.putObject(this.bucket, objectName, tempFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            throw new FileServiceRuntimeException("上传的文件转换为服务器系统文件出错", e);
        }
    }

    /**
     * 合并分片文件，合并完成删除文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    private File getTempFileName(MultipartFile file) throws IOException {
        String path = ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath();
        String fileName = new File(path).getAbsolutePath() + "/tempfiles/" + file.getOriginalFilename();
        File tempFile = new File(fileName);
        if (!tempFile.getParentFile().exists() && !tempFile.getParentFile().mkdirs()) {
            throw new FileServiceRuntimeException("文件夹创建失败");
        }
        file.transferTo(tempFile);
        return tempFile;
    }

    public boolean update(FileInfo fileInfo) {
        return luceneService.update(fileInfo);
    }

    public boolean delete(FileInfo fileInfo) {
        try {
            minioClient.removeObject(bucket, getObjectName(fileInfo));
            return luceneService.delete(fileInfo);
        } catch (Exception e) {
            throw new FileServiceRuntimeException("删除失败", e);
        }
    }

    public List<FileInfo> search(FileInfo fileInfo) {
        return luceneService.search(fileInfo);
    }

    private String getObjectName(FileInfo fileInfo) {
        return StringUtils.join(fileInfo.getUserName(), SysConst.SEP_SLASH, fileInfo.getFileName());
    }

}
