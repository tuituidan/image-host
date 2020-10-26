package com.tuituidan.oss.exception;

import com.tuituidan.oss.util.StringExtUtils;

import ch.qos.logback.classic.spi.EventArgUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * ExceptionBuilder.
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
     * 非public只被ExceptionBuilder使用.
     *
     * @param tip   tip
     * @param error error
     * @param cause cause
     */
    private ImageHostException(String tip, String error, Throwable cause) {
        super(error, cause);
        this.tip = tip;
    }

    String getTip() {
        return this.tip;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String tip;

        private String error;

        private Throwable cause;

        public Builder tip(String msg, Object... args) {
            this.tip = StringExtUtils.format(msg, args);
            return this;
        }

        public Builder error(String msg, Object... args) {
            this.cause = EventArgUtil.extractThrowable(args);
            this.error = StringExtUtils.format(msg, args);
            return this;
        }

        public ImageHostException build() {
            return new ImageHostException(this.tip, this.error, this.cause);
        }
    }
}
