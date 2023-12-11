package com.wms.service.impl;

import com.wms.entity.InStore;
import com.wms.entity.Result;
import com.wms.mapper.InStoreMapper;
import com.wms.mapper.ProductMapper;
import com.wms.mapper.PurchaseMapper;
import com.wms.page.Page;
import com.wms.service.InStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InStoreServiceImpl implements InStoreService {

    // 注入InStoreMapper
    @Autowired
    private InStoreMapper inStoreMapper;

    // 注入ProductMapper
    @Autowired
    private ProductMapper productMapper;

    // 注入PurchaseMapper
    @Autowired
    private PurchaseMapper purchaseMapper;

    // 分页查询入库单的业务方法
    @Override
    public Page queryInStorePage(Page page, InStore inStore) {

        // 查询入库单行数
        Integer count = inStoreMapper.findInStoreCount(inStore);

        // 分页查询入库单
        List<InStore> inStoreList = inStoreMapper.findInStorePage(page, inStore);

        // 封装分页信息
        page.setTotalNum(count);
        page.setResultList(inStoreList);

        return page;
    }

    // 确认入库的业务方法
    @Transactional  // 事务处理
    @Override
    public Result inStoreConfirm(InStore inStore) {

        // 改入库单状态
        int i = inStoreMapper.setIsInById(inStore.getInsId());
        if (i > 0) {
            // 修改商品库存
            int j = productMapper.setInventById(inStore.getProductId(), inStore.getInNum());
            if (j > 0) {
                return Result.ok("入库单确认成功!");
            }
            return Result.err(Result.CODE_ERR_BUSINESS, "入库单确认失败!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "入库单确认失败!");
    }

    // 添加入库单的业务方法
    @Transactional // 事务处理
    @Override
    public Result saveInStore(InStore inStore, Integer buyId) {
        int i = inStoreMapper.insertInStore(inStore);
        if (i > 0) {
            // 修改采购单状态为已入库
            int j = purchaseMapper.setIsById(buyId);
            if (j > 0) {
                return Result.ok("入库单添加成功!");
            }
            return Result.err(Result.CODE_ERR_BUSINESS, "入库单添加失败!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "入库单添加失败!");
    }
}
