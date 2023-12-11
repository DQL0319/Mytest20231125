package com.wms.service.impl;

import com.wms.entity.Brand;
import com.wms.mapper.BrandMapper;
import com.wms.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "com.wms.service.impl.BrandServiceImpl")
@Service
public class BrandServiceImpl implements BrandService {

    // 注入
    @Autowired
    private BrandMapper brandMapper;

    // 查询所有品牌的业务方法
    @Cacheable(key = "'all:brand'")
    @Override
    public List<Brand> queryAllBrand() {
        return brandMapper.findAllBrand();
    }
}
