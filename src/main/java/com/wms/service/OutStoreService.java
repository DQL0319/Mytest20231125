package com.wms.service;

import com.wms.entity.OutStore;
import com.wms.entity.Result;
import com.wms.page.Page;
import org.springframework.beans.factory.annotation.Autowired;

public interface OutStoreService {

    // 添加出库单的业务方法
    public Result saveOutStore(OutStore outStore);

    // 分页查询出库单的业务方法
    public Page queryOutStorePage(Page page, OutStore outStore);

    // 确认出库的业务方法
    public Result outStoreConfirm(OutStore outStore);

}
