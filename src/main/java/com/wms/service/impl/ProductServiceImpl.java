package com.wms.service.impl;

import com.wms.entity.Product;
import com.wms.entity.Result;
import com.wms.mapper.ProductMapper;
import com.wms.page.Page;
import com.wms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    // 注入ProductMapper
    @Autowired
    private ProductMapper productMapper;

    // 分页查询商品的业务方法
    @Override
    public Page queryProductPage(Page page, Product product) {
        // 查询商品行数
        Integer count = productMapper.findProductRowCount(product);
        // 分页查询商品
        List<Product> productList = productMapper.findProductPage(page, product);
        // 组装分页信息
        page.setTotalNum(count);
        page.setResultList(productList);
        return page;
    }

    /*
      添加商品的业务方法
    */
    // 传的图片访问路径的目录路径 -- /img/upLoad/
    @Value("${file.access-path}")
    private String fileAccessPath;

    @Override
    public Result saveProduct(Product product) {
        // 判断商品型号是否已存在
        Product prct = productMapper.findProductByNum(product.getProductNum());
        if (prct != null) {
            return Result.err(Result.CODE_ERR_BUSINESS, "该型号商品已存在!");
        }

        // 处理上传的图片的访问路径
        product.setImgs(fileAccessPath + product.getImgs());
        // 添加商品
        int i = productMapper.insertProduct(product);
        if (i > 0) {
            return Result.ok("商品添加成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "商品添加失败!");
    }

    // 修改商品上下架状态的业务方法
    @Override
    public Result updateStateByPid(Product product) {
        int i = productMapper.setStateByPid(product.getProductId(), product.getUpDownState());
        if (i > 0) {
            return Result.ok("商品上下架状态设置成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "修改商品上下架状态设置失败!");
    }

    // 删除商品的业务方法
    @Override
    public Result deleteProductByIds(List<Integer> productIdList) {
        int i = productMapper.removeProductByIds(productIdList);
        if (i > 0) {
            return Result.ok("商品删除成功!");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "商品删除失败!");
    }
}
