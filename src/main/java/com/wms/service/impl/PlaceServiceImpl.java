package com.wms.service.impl;

import com.wms.entity.Place;
import com.wms.mapper.PlaceMapper;
import com.wms.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.wms.service.impl.PlaceServiceImpl")
@Service
public class PlaceServiceImpl implements PlaceService {

    // 注入
    @Autowired
    private PlaceMapper placeMapper;

    // 查询所有产地的业务方法
    @Cacheable(key = "'all:place'")
    @Override
    public List<Place> queryAllPlace() {
        return placeMapper.findAllPlace();
    }
}
