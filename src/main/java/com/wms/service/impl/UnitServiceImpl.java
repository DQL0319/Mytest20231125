package com.wms.service.impl;

import com.wms.entity.Unit;
import com.wms.mapper.UnitMapper;
import com.wms.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.wms.service.impl.UnitServiceImpl")
@Service
public class UnitServiceImpl implements UnitService {

    // 注入UnitMapper
    @Autowired
    private UnitMapper unitMapper;

    @Cacheable(key = "'all:unit'")
    @Override
    public List<Unit> queryAllUnit() {
        return unitMapper.findAllUnit();
    }
}
