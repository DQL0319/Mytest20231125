package com.wms.controller;

import com.wms.entity.*;
import com.wms.page.Page;
import com.wms.service.InStoreService;
import com.wms.service.PurchaseService;
import com.wms.service.StoreService;
import com.wms.utils.TokenUtils;
import com.wms.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    // 注入PurchaseService
    @Autowired
    private PurchaseService purchaseService;

    // 注入StoreMapper
    @Autowired
    private StoreService storeService;

    // 注入InStoreService
    @Autowired
    private InStoreService inStoreService;

    // 注入
    @Autowired
    private TokenUtils tokenUtils;

    // 添加采购单的urL接口/purchase/purchase-add
    @RequestMapping("/purchase-add")
    public Result addPurchase(@RequestBody Purchase purchase) {
        // 执行业务
        Result result = purchaseService.savePurchase(purchase);
        // 响应
        return result;
    }

    // 查询所有仓库的urL接口/purchase/store-list
    @RequestMapping("/store-list")
    public Result storeList() {
        // 执行业务
        List<Store> storeList = storeService.queryAllStore();
        // 响应
        return Result.ok(storeList);
    }

    // 分页查询采购单的urL接口/purchase/purchase-page-list
    @RequestMapping("/purchase-page-list")
    public Result purchaseListPage(Page page, Purchase purchase) {
        // 执行业务
        page = purchaseService.queryPurchasePage(page, purchase);
        // 响应
        return Result.ok(page);
    }

    // 删除采购单的url接口/purchase/purchase-delete/{buyId}
    @RequestMapping("/purchase-delete/{buyId}")
    public Result deletePurchase(@PathVariable Integer buyId) {
        // 执行业务
        Result result = purchaseService.deletePurchase(buyId);
        // 响应
        return result;
    }

    // 修改采购单的urL接口/purchase/purchase-update
    @RequestMapping("/purchase-update")
    public Result updatePurchase(@RequestBody Purchase purchase) {
        // 执行业务
        Result result = purchaseService.updatePurchaseById(purchase);
        // 响应
        return result;
    }

    // 生成入库单的urL接口/purchase/in-warehouse-record-add
    @RequestMapping("/in-warehouse-record-add")
    public Result addInStore(@RequestBody Purchase purchase, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        // 拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();


        // 创建InStore对象封装入库单信息
        InStore inStore = new InStore();
        inStore.setCreateBy(createBy);
        inStore.setStoreId(purchase.getStoreId());
        inStore.setProductId(purchase.getProductId());
        inStore.setInNum(purchase.getFactBuyNum());

        // 执行业务
        Result result = inStoreService.saveInStore(inStore, purchase.getBuyId());
        // 响应
        return result;
    }
}
