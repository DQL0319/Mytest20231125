package com.wms.service;

import com.wms.entity.Purchase;
import com.wms.entity.Result;
import com.wms.page.Page;

public interface PurchaseService {

    // 添加采购单的业务方法
    public Result savePurchase(Purchase purchase);

    // 分页查询采购单的业务方法
    public Page queryPurchasePage(Page page, Purchase purchase);

    // 删除采购单的业务方法
    public Result deletePurchase(Integer buyId);

    // 修改预计采购数量和实际采购数量的业务方法
    public Result updatePurchaseById(Purchase purchase);
}
