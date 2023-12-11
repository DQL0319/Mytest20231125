package com.wms.service;

import com.wms.entity.Product;
import com.wms.entity.Result;
import com.wms.page.Page;

import java.util.List;

public interface ProductService {

    // 分页查询商品的业务方法
    public Page queryProductPage(Page page, Product product);

    // 添加商品的业务方法
    public Result saveProduct(Product product);

    // 修改商品上下架状态的业务方法
    public Result updateStateByPid(Product product);

    // 删除商品的业务方法
    public Result deleteProductByIds(List<Integer> productIdList);
}
