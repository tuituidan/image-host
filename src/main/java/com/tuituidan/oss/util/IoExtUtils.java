package com.tuituidan.oss.util;

import com.tuituidan.oss.exception.ImageHostException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.IOUtils;

/**
 * IO操作.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/20
 */
@UtilityClass
public class IoExtUtils {

    /**
     * 扩展IOUtils的流转字符串方法，避免业务中捕获异常以及关闭流.
     *
     * @param input input
     * @return String
     */
    public static String toString(InputStream input) {
        try (InputStream sourceIn = input) {
            return IOUtils.toString(sourceIn, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw ImageHostException.builder().error("流转字符串失败", ex).build();
        }
    }

}
