package com.tuituidan.oss.service;

import com.alibaba.fastjson.JSON;
import com.tuituidan.oss.exception.ImageHostException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.stereotype.Service;

/**
 * 查询结果高亮显示.
 *
 * @author zhujunhan
 */
@Service
public class HighLightService implements SearchResultMapper {

    /**
     * 重写数据map类型实现搜索关键字高亮显示.
     *
     * @param response 输出结果
     * @param clazz    结果类型
     * @param pageable 分页信息
     * @param <T>      结果类型
     * @return AggregatedPageImpl
     */
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        SearchHit[] hits = response.getHits().getHits();
        List<T> resultList = new ArrayList<>();
        if (ArrayUtils.isEmpty(hits)) {
            return new AggregatedPageImpl<>(resultList, pageable, response.getHits().getTotalHits());
        }
        //解析出关键字设为高亮文本，放到结果集中
        for (SearchHit searchHit : hits) {
            try {
                T fileDoc = JSON.parseObject(searchHit.getSourceAsString(), clazz);
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                if (MapUtils.isNotEmpty(highlightFields)) {
                    for (Map.Entry<String, HighlightField> item : highlightFields.entrySet()) {
                        BeanUtils.setProperty(fileDoc, item.getKey(), item.getValue().fragments()[0].toString());
                    }
                }
                resultList.add(fileDoc);
            } catch (Exception e) {
                throw ImageHostException.builder().error("标签高亮显示失败", e).build();
            }
        }
        return new AggregatedPageImpl<>(resultList, pageable, response.getHits().getTotalHits());
    }

    @Override
    public <T> T mapSearchHit(SearchHit searchHit, Class<T> clazz) {
        return null;
    }

}

