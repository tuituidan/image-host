package com.tuituidan.oss.kit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 * CompressKit.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/10
 */
@UtilityClass
@Slf4j
public class CompressKit {
    /**
     * 图片质量.
     */
    private static final float QUALITY = 0.3f;

    /**
     * 将源文件进行压缩，这里简单起见直接覆盖源文件.
     *
     * @param srcBytes 源文件
     * @return 转换后的字节输入流
     */
    public static byte[] compress(byte[] srcBytes) {
        try (InputStream srcIn = new ByteArrayInputStream(srcBytes);
             ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            Thumbnails.of(srcIn).scale(1f).outputQuality(QUALITY).toOutputStream(byteOut);
            return byteOut.toByteArray();
        } catch (Exception e) {
            log.error("压缩图片文件出错，将返回原图片流!", e);
            return srcBytes;
        }
    }
}
