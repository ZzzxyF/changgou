package com.zxy.search.dao;
import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

//该接口用户索引数据操作。
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {
    List<SkuInfo> findByName(String name);
}
