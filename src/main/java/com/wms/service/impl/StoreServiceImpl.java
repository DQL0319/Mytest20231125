package com.wms.service.impl;

import com.wms.entity.Store;
import com.wms.mapper.StoreMapper;
import com.wms.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.wms.service.impl.StoreServiceImpl")
@Service
public class StoreServiceImpl implements StoreService {

    // 注入StoreMapper
    @Autowired
    private StoreMapper storeMapper;

    // 询所有仓库的业务方法
    @Cacheable(key = "'all:store'")
    @Override
    public List<Store> queryAllStore() {
        return storeMapper.findAllStore();
    }
}
