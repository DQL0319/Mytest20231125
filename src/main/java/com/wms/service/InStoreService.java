package com.wms.service;

import com.wms.entity.InStore;
import com.wms.entity.Result;
import com.wms.page.Page;

public interface InStoreService {

    // 添加入库单的业务方法
    public Result saveInStore(InStore inStore, Integer buyId);

    // 分页查询入库单的业务方法
    public Page queryInStorePage(Page page, InStore inStore);

    // 确认入库的业务方法
    public Result inStoreConfirm(InStore inStore);
}
