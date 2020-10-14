package com.tuituidan.oss.service;

import com.tuituidan.oss.bean.FileInfo;
import com.tuituidan.oss.exception.base.ExceptionBuilder;
import com.tuituidan.oss.kit.CompressKit;
import com.tuituidan.oss.kit.FileTypeKit;
import com.tuituidan.oss.kit.StringKit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private MinioService minioService;

    @Resource
    private ElasticsearchService elasticsearchService;

    public String upload(FileInfo fileInfo) {
        String ext = FilenameUtils.getExtension(fileInfo.getName()).toLowerCase();
        if (FileTypeKit.isNotSupport(ext)) {
            throw ExceptionBuilder.builder().tip("很抱歉，暂不支持上传该种类型的文件！").build();
        }
        fileInfo.setExt(ext);
        fileInfo.setId(StringKit.getUuid());
        byte[] compressData = getCompressData(getFileData(fileInfo), fileInfo);
        String objName = StringKit.getObjectName(fileInfo.getId(), fileInfo.getExt());
        try (InputStream inputStream = new ByteArrayInputStream(compressData)) {
            minioService.putInputStream(objName, inputStream);
            elasticsearchService.asyncSaveFileDoc(objName, fileInfo);
            return minioService.getObjectUrl(objName);
        } catch (Exception ex) {
            throw ExceptionBuilder.builder().error("上传文件到 MinIO 失败！", ex).build();
        }
    }

    private byte[] getCompressData(byte[] source, FileInfo fileInfo) {
        if (!fileInfo.isCompress()) {
            return source;
        }
        return CompressKit.compress(fileInfo.getExt(), source);
    }

    private byte[] getFileData(FileInfo fileInfo) {
        try {
            // 如果是base64的，转换base64
            if (StringUtils.isNotBlank(fileInfo.getBase64())) {
                return Base64.getDecoder().decode(fileInfo.getBase64());
            }
            return fileInfo.getFile().getBytes();
        } catch (Exception ex) {
            throw ExceptionBuilder.builder().error("获取文件数据失败！", ex).build();
        }
    }


}
