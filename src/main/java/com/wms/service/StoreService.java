package com.wms.service;

import com.wms.entity.Store;

import java.util.List;

public interface StoreService {

    // 询所有仓库的业务方法
    public List<Store> queryAllStore();
}
