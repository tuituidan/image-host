package com.bigbrother.fileservice.utils;

import com.bigbrother.fileservice.consts.SysConst;
import com.bigbrother.fileservice.dto.FileInfo;

import java.util.Date;
import java.util.regex.Pattern;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * @author Administrator
 */
@UtilityClass
public class SysUtil {

    /**
     * CHINESE_PATTERN
     */
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 判断字符串中是否包含中文，不能校验是否为中文标点符号
     *
     * @param str 待校验字符串
     * @return 是否为中文
     */
    public static boolean containChinese(String str) {
        return CHINESE_PATTERN.matcher(str).find();
    }

    public static String getObjectName(FileInfo fileInfo) {
        return StringUtils.join(fileInfo.getUserName(),
                SysConst.SEP_SLASH,
                DateFormatUtils.format(new Date(), "yyyyMMdd"),
                SysConst.SEP_SLASH,
                fileInfo.getId(),
                SysConst.SEP_DOT,
                FilenameUtils.getExtension(fileInfo.getFile().getOriginalFilename())
                );
    }

    public static String getLuceneRoot() {
        return System.getProperty("user.dir").concat("/lucene/");
    }

    public static String getTempFileFolder() {
        return System.getProperty("user.dir").concat("/tempfiles/");
    }


}
