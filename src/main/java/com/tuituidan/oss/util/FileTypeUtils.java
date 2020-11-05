package com.tuituidan.oss.util;

import com.google.common.collect.Sets;
import com.tuituidan.oss.consts.Separator;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;

/**
 * FileTypeKit.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/11
 */
@UtilityClass
@Slf4j
public class FileTypeUtils {

    /**
     * 上传支持的所有文件格式的集合.
     */
    private static final Set<String> SUPPORTED_EXTS = Sets.newHashSet("jpg", "jpeg", "png", "gif", "svg",
            "bmp", "tif", "tiff", "ico", "webp", "swf", "avi", "mp3", "mp4", "wav", "pdf", "json", "html", "js",
            "css", "xml", "txt", "md", "psd", "flv", "mov", "wmv");

    /**
     * 可压缩的文件格式类型的集合.
     */
    private static final Set<String> COMPRESS_EXTS = Sets.newHashSet("jpg", "jpeg", "png");

    /**
     * key 为文件扩展名，value 为MediaType的字符串值的 Map.
     */
    private static Map<String, String> mimeTypeMap;

    static {
        try (InputStream srcIn = new ClassPathResource("config/mime-type.properties").getInputStream()) {
            mimeTypeMap = IOUtils.readLines(srcIn, StandardCharsets.UTF_8).stream()
                    .map(item -> item.split(Separator.EQUAL))
                    .collect(Collectors.toMap(item -> item[0], item -> item[1]));
        } catch (Exception ex) {
            log.error("读取mime-type配置出错！", ex);
        }
    }

    /**
     * 根据文件扩展名获取其对应的MediaType的字符串值， 如果找不到，则默认返回application/octet-stream.
     *
     * @param ext 文件扩展名，不含点 ({@code .}) 号
     * @return MediaType的字符串值
     */
    public static String getMediaTypeValue(String ext) {
        return mimeTypeMap.getOrDefault(ext, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    }

    /**
     * 根据文件扩展名判断是否不支持该种文件类型的上传.
     *
     * @param ext 文件扩展名，不含点 ({@code .}) 号
     * @return 布尔值
     */
    public static boolean isNotSupport(String ext) {
        return !SUPPORTED_EXTS.contains(ext);
    }

    /**
     * 根据文件扩展名判断该文件是否支持压缩.
     *
     * @param ext 文件扩展名，不含点 ({@code .}) 号
     * @return 布尔值
     */
    public static boolean canCompress(String ext) {
        return COMPRESS_EXTS.contains(ext);
    }

    /**
     * 是否png.
     *
     * @param ext 文件扩展名，不含点 ({@code .}) 号
     * @return 布尔值
     */
    public static boolean isPng(String ext) {
        return "png".contains(ext);
    }
}
