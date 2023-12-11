package com.wms.service;

import com.wms.entity.ProductType;
import com.wms.entity.Result;

import java.util.List;

public interface ProductTypeService {

    // 查询所有商品分类树的业务方注
    public List<ProductType> productTypeTree();

    // 校验分类编码是否存在的业务方法
    public Result checkTypeCode(String typeCode);

    // 添加商品分类的业务方法
    public Result saveProductType(ProductType productType);

    // 删除分类的业务方法
    public Result deleteProductType(Integer typeId);

    // 修改商品分类的业务方法
    public Result setProductType(ProductType productType);
}
