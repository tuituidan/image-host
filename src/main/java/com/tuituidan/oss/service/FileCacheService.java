package com.tuituidan.oss.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tuituidan.oss.repository.FileDocRepository;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * FileCacheService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/18
 */
@Service
public class FileCacheService {
    private static final int EXPIRE = 7;

    private static final int MAX_SIZE = 2048;

    /**
     * 内部的缓存对象，{@code key} 是文件的 {@code MD5} 值，{@code value} 是在 MinIO 中的图片链接地址.
     */
    private static final Cache<String, String> CACHE = Caffeine.newBuilder()
            .expireAfterWrite(EXPIRE, TimeUnit.DAYS)
            .expireAfterAccess(EXPIRE, TimeUnit.DAYS)
            .maximumSize(MAX_SIZE)
            .build();

    @Resource
    private FileDocRepository fileDocRepository;

    @PostConstruct
    private void init() {
        fileDocRepository.findAll().forEach(item ->
                CACHE.put(item.getMd5(), item.getPath())
        );
    }

    /**
     * put.
     *
     * @param md5      md5
     * @param url      url
     */
    public void put(String md5, String url) {
        CACHE.put(md5, url);
    }

    /**
     * get.
     *
     * @param md5 md5
     * @return String
     */
    public String get(String md5) {
        return CACHE.getIfPresent(md5);
    }
}
