package com.zxy.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import com.google.common.collect.Maps;
import com.zxy.entity.Result;
import com.zxy.feign.SkuFeign;
import com.zxy.pojo.Sku;
import com.zxy.search.config.EsConfig;
import com.zxy.search.dao.SkuEsMapper;
import com.zxy.search.service.SkuService;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    SkuFeign skuFeign;
    @Autowired
    public SkuEsMapper skuEsMapper;
    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Autowired
    public RestHighLevelClient restHighLevelClient;
    @Override
    public void importSku() {
        //skufeign查询出数据
        Result<List<Sku>> skuList= skuFeign.findByStatus("1");
        //sku_es插入数据,将list转为其他的List
        List<SkuInfo> skuInfos=JSON.parseArray(JSON.toJSONString(skuList.getData()), SkuInfo.class);
        for(SkuInfo skuInfo:skuInfos){
            Map<String,Object>  specMap=JSON.parseObject(skuInfo.getSpec());
            skuInfo.setSpecMap(specMap);
        }
        //es data
        skuEsMapper.saveAll(skuInfos);
    }

    //搜索方法
    @Override
    public Map search(Map<String, String> searchMap) throws IOException {
        String keywords=searchMap.get("keywords");
        if(StringUtils.isEmpty(keywords)){
            keywords="华为";//默认搜索内容
        }
        //方式1：
  /*      Map mapResult= Maps.newHashMap();
        List<SkuInfo> skuInfos= skuEsMapper.findByName(searchKey);
        mapResult.put("total",skuInfos);*/
        //方式2：
        /*SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.termQuery("name",searchKey));
        searchRequest.indices("skuinfo").types("docs").source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = search.getHits().getHits();
        long total=search.getHits().totalHits;
        Map map=Maps.newHashMap();
        map.put("rows",hits);
        map.put("total",total);
        map.put("totalPages",1);
        return map;*/
        //2.创建查询对象 的构建对象
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //3.设置查询的条件
        //分类分组搜索条件设置
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategoryGroup").field("categoryName").size(50));
        //品牌分组搜索条件设置
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrandGroup").field("brandName").size(50));
        //规格分组搜索条件设置
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpecGroup").field("spec.keyword").size(100));
        nativeSearchQueryBuilder.withQuery(QueryBuilders.matchQuery("name", keywords));//根据name查询
        //分页
        Integer pageNumber=1;
        String pageNumberT=searchMap.get("pageNum");//页数
        if(!StringUtils.isEmpty(pageNumberT)){
            pageNumber = Integer.valueOf(searchMap.get("pageNum"));
        }
        Integer pageSize=10;
        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNumber - 1, pageSize));
        //过滤
        BoolQueryBuilder boolQueryBuilder =QueryBuilders.boolQuery();
        if(!StringUtils.isEmpty(searchMap.get("brand"))){//品牌
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName",searchMap.get("brand")));
        }
        if(!StringUtils.isEmpty(searchMap.get("category"))){//分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName",searchMap.get("category")));
        }

        //范围查询---价格
        String price=searchMap.get("price");
        if(!StringUtils.isEmpty(price)){
            String[] prices=price.split("-");
            if(prices.length>1){
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(prices[0],true).to(prices[1],true));
            }else{
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(prices[0]));
            }
        }
        //排序
        String sortField=searchMap.get("sortField");//排序字段
        String sortRule=searchMap.get("sortRule");//排序方式
        if (!StringUtils.isEmpty(sortRule) && !StringUtils.isEmpty(sortField)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortField).order(sortRule.equals("DESC") ? SortOrder.DESC : SortOrder.ASC));
        }
        //设置高亮条件
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder().preTags("<em style=\"color:red\">").postTags("</em>"));

        //设置主关键字查询--》多字段查询
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"name","brandName","categoryName"));
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);//添加过滤
        //4.构建查询对象
        NativeSearchQuery query = nativeSearchQueryBuilder.build();
        //5.执行查询
        //AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class);

        AggregatedPage<SkuInfo> skuPage = esTemplate.queryForPage(query, SkuInfo.class, new SearchResultMapperImpl());

        //6.返回结果
        //获取分组结果
        StringTerms skuCategoryTerms= (StringTerms) skuPage.getAggregation("skuCategoryGroup");//分类分组信息
        StringTerms skuBrandTerms= (StringTerms) skuPage.getAggregation("skuBrandGroup");//品牌分组信息
        StringTerms skuSpecTerms=(StringTerms) skuPage.getAggregation("skuSpecGroup");//规格分组信息
        List<String> categoryList=getCategoryList(skuCategoryTerms);
        List<String> brandList=getStringsBrandList(skuBrandTerms);
        Map<String, Set<String>> specMap=getStringSetMap(skuSpecTerms);
        Map resultMap = new HashMap<>();
        resultMap.put("specMap",specMap);//品牌分类集合
        resultMap.put("categoryList",categoryList);//种类分类集合
        resultMap.put("brandList",brandList);//品牌分类集合
        resultMap.put("rows", skuPage.getContent());
        resultMap.put("total", skuPage.getTotalElements());
        resultMap.put("totalPages", skuPage.getTotalPages());
        //设置分页数据
        resultMap.put("pageNumber",pageNumber);
        resultMap.put("pageSize",pageSize);
        return resultMap;
    }

    //映射结果集
    public class SearchResultMapperImpl implements SearchResultMapper {
        @Override
        public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
            List<T> content = new ArrayList<>();
            //如果没有结果返回为空
            if (response.getHits() == null || response.getHits().getTotalHits() <= 0) {
                return new AggregatedPageImpl<T>(content);
            }
            for (SearchHit searchHit : response.getHits()) {
                String sourceAsString = searchHit.getSourceAsString();
                SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);

                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                HighlightField highlightField = highlightFields.get("name");

                //有高亮则设置高亮的值
                if (highlightField != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text text : highlightField.getFragments()) {
                        stringBuffer.append(text.string());
                    }
                    skuInfo.setName(stringBuffer.toString());
                }
                content.add((T) skuInfo);
            }


            return new AggregatedPageImpl<T>(content, pageable, response.getHits().getTotalHits(), response.getAggregations(), response.getScrollId());
        }
    }

    public List<String>  getCategoryList(StringTerms StrTerms){
        List<String> categoryList=Lists.newArrayList();
        if(StrTerms!=null){
            for (StringTerms.Bucket bucket : StrTerms.getBuckets()) {
                String keyAsString = bucket.getKeyAsString();//分组的值
                categoryList.add(keyAsString);}
        }
        return categoryList;
    }
    private List<String> getStringsBrandList(StringTerms stringTermsBrand) {
        List<String> brandList = new ArrayList<>();
        if (stringTermsBrand != null) {
            for (StringTerms.Bucket bucket : stringTermsBrand.getBuckets()) {
                brandList.add(bucket.getKeyAsString());
            }
        }
        return brandList;
    }

    /**
     * 获取规格列表数据
     *
     * @param stringTermsSpec
     * @return
     */
    private Map<String, Set<String>> getStringSetMap(StringTerms stringTermsSpec) {
        Map<String, Set<String>> specMap = new HashMap<String, Set<String>>();
        Set<String> specList = new HashSet<>();
        if (stringTermsSpec != null) {
            for (StringTerms.Bucket bucket : stringTermsSpec.getBuckets()) {
                specList.add(bucket.getKeyAsString());
            }
        }
        for (String specjson : specList) {
            Map<String, String> map = JSON.parseObject(specjson, Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {//
                String key = entry.getKey();        //规格名字
                String value = entry.getValue();    //规格选项值
                //获取当前规格名字对应的规格数据
                Set<String> specValues = specMap.get(key);
                if (specValues == null) {
                    specValues = new HashSet<String>();
                }
                //将当前规格加入到集合中
                specValues.add(value);
                //将数据存入到specMap中
                specMap.put(key, specValues);
            }
        }
        return specMap;
    }
}
