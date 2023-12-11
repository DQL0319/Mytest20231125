package com.wms.service;

import com.wms.entity.Supply;

import java.util.List;

public interface SupplyService {

    // 查询所有供应商的业务方法
    public List<Supply> queryAllSupply();
}
