package com.tuituidan.oss.service;

import com.tuituidan.oss.bean.FileInfo;
import com.tuituidan.oss.exception.ImageHostException;
import com.tuituidan.oss.kit.*;

import io.minio.messages.Tags;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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

    /**
     * base64数据上传.
     *
     * @param inputStream inputStream
     * @return String
     */
    public String uploadBase64(InputStream inputStream) {
        String base64Str;
        try (InputStream sourceIn = inputStream) {
            base64Str = IOUtils.toString(sourceIn, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw ImageHostException.builder().error("获取base64数据失败", ex).build();
        }
        Pair<String, String> base64 = StringKit.getBase64Info(base64Str);
        if (null == base64) {
            throw ImageHostException.builder().error("base64数据格式错误-{}", base64Str).build();
        }
        return upload(new FileInfo().setName("image." + base64.getLeft()).setBase64(base64.getRight()));
    }

    /**
     * 文件上传.
     * TODO 分片上传
     * TODO 秒传
     * TODO 断点续传
     *
     * @param fileInfo fileInfo
     * @return String
     */
    public String upload(FileInfo fileInfo) {
        String ext = FilenameUtils.getExtension(fileInfo.getName()).toLowerCase();
        if (FileTypeKit.isNotSupport(ext)) {
            throw ImageHostException.builder().tip("很抱歉，暂不支持上传该种类型的文件！").build();
        }
        fileInfo.setExt(ext);
        fileInfo.setId(IdKit.getId());
        byte[] sourceData = getDataFromFileInfo(fileInfo);
        String md5 = DigestUtils.md5Hex(sourceData);
        // TODO 这里判断原始文件【md5+是否压缩】是否已经存在，存在直接返回已有的数据实现不重复上传
        if (fileInfo.isCompress()) {
            sourceData = CompressKit.compress(fileInfo.getExt(), sourceData);
        }
        String objName = StringKit.getObjectName(fileInfo.getId(), fileInfo.getExt());
        try (InputStream inputStream = new ByteArrayInputStream(sourceData)) {
            Map<String, String> tags = HashMapKit.newFixQuarterSize();
            tags.put("info", fileInfo.getTags());
            tags.put("compress", String.valueOf(fileInfo.isCompress()));
            tags.put("md5", md5);
            minioService.putObject(objName, Tags.newObjectTags(tags), inputStream);
            elasticsearchService.asyncSaveFileDoc(objName, fileInfo);
            return minioService.getObjectUrl(objName);
        } catch (Exception ex) {
            throw ImageHostException.builder().error("上传文件到 MinIO 失败！", ex).build();
        }
    }

    private byte[] getDataFromFileInfo(FileInfo fileInfo) {
        try {
            // 如果是base64的，转换base64
            if (StringUtils.isNotBlank(fileInfo.getBase64())) {
                return Base64.getDecoder().decode(fileInfo.getBase64());
            }
            return fileInfo.getFile().getBytes();
        } catch (Exception ex) {
            throw ImageHostException.builder().error("获取文件数据失败！", ex).build();
        }
    }
}
