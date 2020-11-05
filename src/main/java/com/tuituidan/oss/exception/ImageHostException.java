package com.tuituidan.oss.exception;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.tuituidan.oss.util.StringExtUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 使用建造者模式来构建异常，让打印日志信息和前端提示分开.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Slf4j
public class ImageHostException extends RuntimeException {

    private static final long serialVersionUID = -6611783498373065076L;

    private final String tip;

    /**
     * 格式化错误信息.
     *
     * @param error error
     * @param args args
     */
    public ImageHostException(String error, Object... args) {
        super(StringExtUtils.format(error, args), EventArgUtil.extractThrowable(args));
        this.tip = null;
    }

    /**
     * 非public只被ExceptionBuilder使用.
     *
     * @param tip tip
     * @param error error
     * @param cause cause
     */
    private ImageHostException(String tip, String error, Throwable cause) {
        super(error, cause);
        this.tip = tip;
    }

    /**
     * getTip.
     *
     * @return String
     */
    String getTip() {
        return this.tip;
    }

    /**
     * builder.
     *
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder.
     */
    public static class Builder {

        private String tip;

        private String error;

        private Throwable cause;

        /**
         * tip.
         *
         * @param msg msg
         * @param args args
         * @return Builder
         */
        public Builder tip(String msg, Object... args) {
            this.tip = StringExtUtils.format(msg, args);
            return this;
        }

        /**
         * error.
         *
         * @param msg msg
         * @param args args
         * @return Builder
         */
        public Builder error(String msg, Object... args) {
            this.cause = EventArgUtil.extractThrowable(args);
            this.error = StringExtUtils.format(msg, args);
            return this;
        }

        /**
         * build.
         *
         * @return ImageHostException
         */
        public ImageHostException build() {
            return new ImageHostException(this.tip, this.error, this.cause);
        }
    }
}
