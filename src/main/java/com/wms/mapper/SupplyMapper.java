package com.wms.mapper;

import com.wms.entity.Supply;

import java.util.List;

public interface SupplyMapper {

    // 查询所有供应商的方法
    public List<Supply> findAllSupply();
}
