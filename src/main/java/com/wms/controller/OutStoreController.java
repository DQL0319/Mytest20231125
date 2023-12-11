package com.wms.controller;

import com.wms.entity.CurrentUser;
import com.wms.entity.OutStore;
import com.wms.entity.Result;
import com.wms.entity.Store;
import com.wms.page.Page;
import com.wms.service.OutStoreService;
import com.wms.service.StoreService;
import com.wms.utils.TokenUtils;
import com.wms.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/outstore")
public class OutStoreController {

    // 注入OutStoreService
    @Autowired
    private OutStoreService outStoreService;

    // 注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    // 注入StoreService
    @Autowired
    private StoreService storeService;

    // 添加出库单的urL接口/outstore/outstore-add
    @RequestMapping("/outstore-add")
    public Result addOutStore(@RequestBody OutStore outStore, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        // 拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();
        outStore.setCreateBy(createBy);
        Result result = outStoreService.saveOutStore(outStore);
        return result;
    }

    // 查询所有仓库的url接口/outstore/store-list
    @RequestMapping("/store-list")
    public Result storeList() {
        List<Store> storeList = storeService.queryAllStore();

        return Result.ok(storeList);
    }

    // 分页查询出库单的url接口/outstore/outstore-page-list
    @RequestMapping("/outstore-page-list")
    public Result outStoreListPage(Page page, OutStore outStore) {
        // 执行业务
        page = outStoreService.queryOutStorePage(page, outStore);

        // 响应
        return Result.ok(page);
    }

    // 确认出库的url接口/outstore/outstore-confirm
    @RequestMapping("/outstore-confirm")
    public Result confirmOutStore(@RequestBody OutStore outStore) {
        // 执行业务
        Result result = outStoreService.outStoreConfirm(outStore);

        // 响应
        return result;
    }
}
