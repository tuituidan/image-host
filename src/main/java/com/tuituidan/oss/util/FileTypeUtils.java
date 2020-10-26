package com.tuituidan.oss.util;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;

/**
 * FileTypeKit.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/11
 */
@UtilityClass
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
     * key 为文件扩展名，value 为 {@link MediaType} 的字符串值的 Map.
     */
    private static final Map<String, String> EXT_MEDIA_MAP = new HashMap<>();

    static {
        EXT_MEDIA_MAP.put("jpg", "image/jpeg");
        EXT_MEDIA_MAP.put("jpeg", "image/jpeg");
        EXT_MEDIA_MAP.put("png", "image/png");
        EXT_MEDIA_MAP.put("gif", "image/gif");
        EXT_MEDIA_MAP.put("svg", "image/svg+xml");
        EXT_MEDIA_MAP.put("bmp", "image/bmp");
        EXT_MEDIA_MAP.put("tif", "image/tiff");
        EXT_MEDIA_MAP.put("tiff", "image/tiff");
        EXT_MEDIA_MAP.put("ico", "image/x-icon");
        EXT_MEDIA_MAP.put("webp", "image/webp");
        EXT_MEDIA_MAP.put("swf", "application/x-shockwave-flash");
        EXT_MEDIA_MAP.put("avi", "video/x-msvideo");
        EXT_MEDIA_MAP.put("mp3", "audio/mpeg");
        EXT_MEDIA_MAP.put("mp4", "video/mp4");
        EXT_MEDIA_MAP.put("wav", "audio/wav");
        EXT_MEDIA_MAP.put("pdf", "application/pdf");
        EXT_MEDIA_MAP.put("json", "application/json;charset=UTF-8");
        EXT_MEDIA_MAP.put("html", "text/html");
        EXT_MEDIA_MAP.put("js", "text/plain");
        EXT_MEDIA_MAP.put("css", "text/css");
        EXT_MEDIA_MAP.put("xml", "text/xml");
        EXT_MEDIA_MAP.put("txt", "text/plain");
        EXT_MEDIA_MAP.put("md", "text/markdown");
    }

    /**
     * 根据文件扩展名获取其对应的 {@link MediaType} 的字符串值，
     * 如果找不到，则默认返回 {@code application/octet-stream} 值.
     *
     * @param ext 文件扩展名，不含点 ({@code .}) 号
     * @return {@link MediaType} 的字符串值
     */
    public static String getMediaTypeValue(String ext) {
        String mediaTypeValue = EXT_MEDIA_MAP.get(ext);
        return mediaTypeValue != null ? mediaTypeValue : MediaType.APPLICATION_OCTET_STREAM_VALUE;
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
