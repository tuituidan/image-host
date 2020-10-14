package com.tuituidan.oss.config;

import com.tuituidan.oss.exception.ImageHostException;

import lombok.Setter;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * ElasticsearchConfig.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@EnableElasticsearchRepositories
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private String elasticEndpoint;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        try (RestHighLevelClient rest = RestClients.create(ClientConfiguration.builder().connectedTo(elasticEndpoint).build()).rest()) {
            return rest;
        } catch (Exception ex) {
            throw ImageHostException.builder().error("elasticsearchClient创建失败", ex).build();
        }
    }
}
