package com.wms.service.impl;

import com.wms.entity.ProductType;
import com.wms.entity.Result;
import com.wms.mapper.ProductTypeMapper;
import com.wms.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@CacheConfig(cacheNames = "com.wms.service.impl.ProductTypeServiceImpl")
@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    // 注入ProductTypeMapper
    @Autowired
    private ProductTypeMapper productTypeMapper;

    // 查询所有商品分类树的业务方注
    @Cacheable(key = "'all:typeTree'")
    @Override
    public List<ProductType> productTypeTree() {
        // 查出所有商品分类
        List<ProductType> allProductTypeList = productTypeMapper.findAllProductType();
        // 将所有商品分类转成商品分类树
        List<ProductType> typeList = allTypeToTypeTree(allProductTypeList, 0);
        return typeList;
    }

    // 将所有商品分类转成商品分类树的递归算法
    private List<ProductType> allTypeToTypeTree(List<ProductType> typeList, Integer pid) {
        // 拿到所有一级分类
        List<ProductType> firstLevelType = new ArrayList<>();
        for (ProductType productType : typeList) {
            if (productType.getParentId().equals(pid)) {
                firstLevelType.add(productType);
            }
        }

        // 出每个一级分类下的所有二级分类
        for (ProductType productType : firstLevelType) {
            List<ProductType> secondLevelType = allTypeToTypeTree(typeList, productType.getTypeId());
            productType.setChildProductCategory(secondLevelType);
        }

        return firstLevelType;
    }

    // 校验分类编码是否存在的业务方法
    @Override
    public Result checkTypeCode(String typeCode) {

        // 根据分类编码查询分类，并判断是否存在
        ProductType productType = new ProductType();
        productType.setTypeCode(typeCode);
        ProductType prodType = productTypeMapper.findTypeByCodeOrName(productType);

        return Result.ok(prodType == null);
    }

    // 添加商品分类的业务方法
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result saveProductType(ProductType productType) {

        // 校验分类名称是否已存在
        ProductType prodType = new ProductType();
        prodType.setTypeName(productType.getTypeName());
        ProductType prodCategory = productTypeMapper.findTypeByCodeOrName(prodType);
        if (prodCategory != null) {
            return Result.err(Result.CODE_ERR_BUSINESS, "分类名称已存在!");
        }
        int i = productTypeMapper.insertProductType(productType);
        if (i > 0) {
            return Result.ok("添加商品分类成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "添加商品分类失败!");
    }

    // 删除分类的业务方法
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result deleteProductType(Integer typeId) {
        int i = productTypeMapper.removeProductType(typeId);
        if (i > 0) {
            return Result.ok("商品分类删除成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "商品分类删除失败!");
    }

    // 修改商品分类的业务方法
    @CacheEvict(key = "'all:typeTree'")
    @Override
    public Result setProductType(ProductType productType) {

        // 修改的分类名称是否已经存在
        ProductType prodType = new ProductType();
        prodType.setTypeName(productType.getTypeName());
        ProductType prodCategory = productTypeMapper.findTypeByCodeOrName(prodType);
        if (prodCategory != null && !prodCategory.getTypeId().equals(productType.getTypeId())) {
            return Result.err(Result.CODE_ERR_BUSINESS, "分类名称已存在!");
        }
        int i = productTypeMapper.setProductTypeById(productType);
        if (i > 0) {
            return Result.ok("修改商品分类成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "修改商品分类失败!");
    }
}
