package com.tuituidan.oss.repository;

import com.tuituidan.oss.bean.FileDoc;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * FileDocRepository.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
public interface FileDocRepository extends ElasticsearchRepository<FileDoc, String> {
}
