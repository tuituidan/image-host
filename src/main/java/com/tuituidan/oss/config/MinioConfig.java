package com.tuituidan.oss.config;

import io.minio.MinioClient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinioConfig.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/8/10
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucket;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }

}
