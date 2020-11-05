package com.tuituidan.oss.util;

import com.tuituidan.oss.exception.ImageHostException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * ResponseUtils.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/31
 */
@UtilityClass
public class ResponseUtils {

    private static final String USER_AGENT = "User-Agent";

    private static final String IE_MSIE = "MSIE";

    private static final String IE_TRIDENT = "Trident";

    /**
     * 下载方法.
     *
     * @param fileName 文件名
     * @param inputStream 文件流
     */
    public static void download(String fileName, InputStream inputStream) {
        try (InputStream in = inputStream) {
            IOUtils.copy(in, getHttpResponse(fileName).getOutputStream());
        } catch (Exception ex) {
            throw ImageHostException.builder().error("下载失败", ex).build();
        }
    }

    /**
     * 获取HttpServletResponse，并设置filename.
     *
     * @param fileName 输出文件名
     * @return HttpServletResponse
     */
    private static HttpServletResponse getHttpResponse(String fileName) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes)) {
            throw new ClassCastException("获取ServletRequestAttributes失败");
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == sra) {
            throw new NullPointerException("获取HttpServletResponse失败");
        }
        HttpServletResponse response = sra.getResponse();
        if (null == response) {
            throw new NullPointerException("获取HttpServletResponse失败");
        }
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        if (StringUtils.isBlank(fileName)) {
            return response;
        }
        HttpServletRequest request = sra.getRequest();
        String userAgent = request.getHeader(USER_AGENT);
        if (StringUtils.containsAny(userAgent, IE_MSIE, IE_TRIDENT, IE_MSIE)) {
            fileName = StringExtUtils.urlEncoder(fileName);
        } else {
            fileName = StringExtUtils.toIso88591(fileName);
        }
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        return response;
    }
}
