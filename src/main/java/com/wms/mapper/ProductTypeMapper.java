package com.wms.mapper;

import com.wms.entity.ProductType;

import java.util.List;

public interface ProductTypeMapper {

    // 查询所有商品分类的方法
    public List<ProductType> findAllProductType();

    // 根据分类编码或者分类名称查询商品分类的方法
    public ProductType findTypeByCodeOrName(ProductType productType);

    // 添加商品分类的方法
    public int insertProductType(ProductType productType);

    // 根据分类id或父级分类id删除分类的方法
    public int removeProductType(Integer typeId);

    // 根据分类id修改分类的方法
    public int setProductTypeById(ProductType productType);
}
