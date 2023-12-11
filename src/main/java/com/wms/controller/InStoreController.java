package com.wms.controller;

import com.wms.entity.InStore;
import com.wms.entity.Result;
import com.wms.entity.Store;
import com.wms.page.Page;
import com.wms.service.InStoreService;
import com.wms.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/instore")
public class InStoreController {

    // 注入InStoreService
    @Autowired
    private InStoreService inStoreService;

    // 注入
    @Autowired
    private StoreService storeService;

    // 查询所有仓库的urL接口/instore/store-list
    @RequestMapping("/store-list")
    public Result storeList() {
        List<Store> storeList = storeService.queryAllStore();
        return Result.ok(storeList);
    }

    // 分页查询入库单的urL接口/instore/instore-page-list
    @RequestMapping("/instore-page-list")
    public Result inStoreListPage(Page page, InStore inStore) {
        // 执行业务
        page = inStoreService.queryInStorePage(page, inStore);
        // 响应
        return Result.ok(page);
    }

    // 确认入库的urL接口/instore/instore-confirm
    @RequestMapping("/instore-confirm")
    public Result confirmInStore(@RequestBody InStore inStore) {
        // 执行业务
        Result result = inStoreService.inStoreConfirm(inStore);
        // 响应
        return result;
    }

}
