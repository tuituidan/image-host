package com.tuituidan.oss.service;

import com.tuituidan.oss.bean.FileDoc;
import com.tuituidan.oss.bean.FileInfo;
import com.tuituidan.oss.bean.FileQuery;
import com.tuituidan.oss.consts.Consts;
import com.tuituidan.oss.repository.FileDocRepository;
import com.tuituidan.oss.util.BeanExtUtils;
import com.tuituidan.oss.util.ResponseUtils;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * ElasticsearchService.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@Service
public class ElasticsearchService {

    @Resource
    private MinioService minioService;

    @Resource
    private FileDocRepository fileDocRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private FileCacheService fileCacheService;

    @PostConstruct
    private void init() {
        if (elasticsearchRestTemplate.indexExists(Consts.ES_INDEX)) {
            fileDocRepository.findAll().forEach(item -> {
                if (StringUtils.isNotBlank(item.getMd5())) {
                    fileCacheService.put(item.getMd5(), item.getPath());
                }
            });
        }
    }

    /**
     * 保存文件信息.
     *
     * @param objName  objName
     * @param md5      md5
     * @param fileInfo fileInfo
     */
    public void saveFileDoc(String objName, String md5, FileInfo fileInfo) {
        FileDoc fileDoc = BeanExtUtils.convert(fileInfo, FileDoc.class);
        fileDoc.setPath(objName);
        fileDoc.setMd5(md5);
        fileDoc.setCreateDate(new Date());
        fileDocRepository.save(fileDoc);
    }

    /**
     * 根据标签查询文件.
     *
     * @param fileQuery 查询参数
     * @return List
     */
    public Page<FileDoc> search(FileQuery fileQuery) {
        // 拼接查询参数
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder()
                .withHighlightFields()
                // 分页查询
                .withPageable(PageRequest.of(fileQuery.getPageIndex(), fileQuery.getPageSize()))
                // 排序
                .withSort(SortBuilders.scoreSort());
        if (StringUtils.isNotBlank(fileQuery.getTags())) {
            // 高亮标签显示
            searchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("tags")
                    .preTags("<span style=\"color:red\">").postTags("</span>"));
            searchQueryBuilder.withQuery(QueryBuilders.matchQuery("tags", fileQuery.getTags()))
                    .withSort(SortBuilders.scoreSort());
        } else {
            searchQueryBuilder.withSort(SortBuilders.fieldSort("createDate").order(SortOrder.DESC));
        }
        Page<FileDoc> fileDocs = elasticsearchRestTemplate.queryForPage(searchQueryBuilder.build(), FileDoc.class);
        if (CollectionUtils.isNotEmpty(fileDocs.getContent())) {
            fileDocs.getContent().forEach(item -> item.setPath(minioService.getObjectUrl(item.getPath())));
        }
        return fileDocs;
    }

    /**
     * 修改标签接口.
     *
     * @param id  id
     * @param tags tags
     */
    public void update(String id, String tags) {
        FileDoc fileDoc = fileDocRepository.findById(id).orElse(null);
        if (null != fileDoc) {
            fileDoc.setTags(tags);
            fileDocRepository.save(fileDoc);
            minioService.updateTags(fileDoc.getPath(), tags);
        }
    }

    /**
     * delete.
     *
     * @param id 需要删除的文件id
     */
    public void delete(String id) {
        FileDoc fileDoc = fileDocRepository.findById(id).orElse(null);
        if (null != fileDoc) {
            fileCacheService.remove(fileDoc.getMd5());
            fileDocRepository.deleteById(id);
            minioService.deleteObject(fileDoc.getPath());
        }
    }

    /**
     * 文件下载.
     *
     * @param id id
     */
    public void download(String id) {
        FileDoc fileDoc = fileDocRepository.findById(id).orElseThrow(NullPointerException::new);
        ResponseUtils.download(fileDoc.getName(), minioService.getObject(fileDoc.getPath()));
    }

}
