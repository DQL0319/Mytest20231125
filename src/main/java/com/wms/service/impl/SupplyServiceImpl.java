package com.wms.service.impl;

import com.wms.entity.Supply;
import com.wms.mapper.SupplyMapper;
import com.wms.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.wms.service.impl.SupplyServiceImpl")
@Service
public class SupplyServiceImpl implements SupplyService {

    // 注入SupplyMapper
    @Autowired
    private SupplyMapper supplyMapper;

    // 查询所有供应商的业务方法
    @Cacheable(key = "'all:supply'")
    @Override
    public List<Supply> queryAllSupply() {
        return supplyMapper.findAllSupply();
    }
}
