package com.tuituidan.oss.kit;

import com.idrsolutions.image.png.PngCompressor;

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
     * 将图片进行压缩.
     *
     * @param srcBytes 源文件
     * @return 转换后的字节输入流
     */
    public static byte[] compress(String fileExt, byte[] srcBytes) {
        if (!FileTypeKit.canCompress(fileExt)) {
            return srcBytes;
        }
        if (FileTypeKit.isPng(fileExt)) {
            return compressPng(srcBytes);
        }
        return compressJpg(srcBytes);
    }

    private static byte[] compressJpg(byte[] srcBytes) {
        try (InputStream srcIn = new ByteArrayInputStream(srcBytes);
             ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            Thumbnails.of(srcIn).scale(1f).outputQuality(QUALITY).toOutputStream(byteOut);
            return byteOut.toByteArray();
        } catch (Exception e) {
            log.error("压缩图片文件出错，将返回原图片流!", e);
            return srcBytes;
        }
    }

    private static byte[] compressPng(byte[] datas) {
        try (InputStream srcIn1 = new ByteArrayInputStream(datas);
             ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {
            PngCompressor.compress(srcIn1, byteOut);
            return byteOut.toByteArray();
        } catch (Exception e) {
            log.error("压缩图片文件出错，将返回原图片流!", e);
            return datas;
        }
    }
}
