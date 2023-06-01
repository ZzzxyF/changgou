package com.zxy.search.service;

import java.io.IOException;
import java.util.Map;

public interface SkuService {
    //导入sku信息
    public void importSku();
    //搜索信息
    Map search(Map<String,String> searchMap) throws IOException;
}
