package com.wms.service.impl;

import com.wms.entity.Purchase;
import com.wms.entity.Result;
import com.wms.mapper.PurchaseMapper;
import com.wms.page.Page;
import com.wms.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    // 注入PurchaseMapper
    @Autowired
    private PurchaseMapper purchaseMapper;

    // 添加采购单的业务方法
    @Override
    public Result savePurchase(Purchase purchase) {

        /*// 补充字段--给fact_buy_num字段实际采购数量赋值为buy_num字段的值预采购数量
        purchase.setFactBuyNum(purchase.getBuyNum());*/

        int i = purchaseMapper.insertPurchase(purchase);
        if (i > 0) {
            return Result.ok("采购单添加成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "采购单添加失败!");
    }

    // 分页查询采购单的业务方法
    @Override
    public Page queryPurchasePage(Page page, Purchase purchase) {

        // 查询采购单行数
        Integer count = purchaseMapper.findPurchaseCount(purchase);

        // 页查询采购单
        List<Purchase> purchaseList = purchaseMapper.findPurchasePage(page, purchase);

        page.setTotalNum(count);
        page.setResultList(purchaseList);

        return page;
    }

    // 删除采购单的业务方法
    @Override
    public Result deletePurchase(Integer buyId) {
        int i = purchaseMapper.removePurchaseById(buyId);
        if (i > 0) {
            return Result.ok("采购单删除成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "采购单删除失败!");
    }

    // 修改预计采购数量和实际采购数量的业务方法
    @Override
    public Result updatePurchaseById(Purchase purchase) {
        int i = purchaseMapper.setNumById(purchase);
        if (i > 0) {
            return Result.ok("采购单修改成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "采购单修改失败!");
    }
}
