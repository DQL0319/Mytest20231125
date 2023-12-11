package com.wms.controller;

import com.wms.entity.*;
import com.wms.page.Page;
import com.wms.service.*;
import com.wms.utils.TokenUtils;
import com.wms.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    // 注入StoreService
    @Autowired
    private StoreService storeService;

    // 注入BrandService
    @Autowired
    private BrandService brandService;

    // 注入ProductService
    @Autowired
    private ProductService productService;

    // 注入ProductTypeService
    @Autowired
    private ProductTypeService productTypeService;

    // 注入SupplyService
    @Autowired
    private SupplyService supplyService;

    // 注入PlaceService
    @Autowired
    private PlaceService placeService;

    // 注入UnitService
    @Autowired
    private UnitService unitService;

    // 注入TokenUtils
    @Autowired
    private TokenUtils tokenUtils;

    // 查询所有仓库的UrL接口/product/store-list
    @RequestMapping("/store-list")
    public Result storeList() {
        // 执行业务
        List<Store> storelist = storeService.queryAllStore();
        // 响应
        return Result.ok(storelist);
    }

    // 查询所有品牌的urL接口/product/brand-list
    @RequestMapping("/brand-list")
    public Result brandList() {
        // 执行业务
        List<Brand> brandlist = brandService.queryAllBrand();
        // 响应
        return Result.ok(brandlist);
    }

    // 分页查询商品的url接口/product/product-page-list
    @RequestMapping("/product-page-list")
    public Result productPageList(Page page, Product product) {
        // 执行业务
        page = productService.queryProductPage(page, product);
        // 响应
        return Result.ok(page);
    }

    // 查询所有商品分类树的urL接口/product/category-tree
    @RequestMapping("/category-tree")
    public Result loadTypeTree() {
        // 执行业务
        List<ProductType> typeTreeList = productTypeService.productTypeTree();
        // 响应
        return Result.ok(typeTreeList);
    }

    // 查询所有供应商的urL接口/product/suppLy-list
    @RequestMapping("/supply-list")
    public Result supplyList() {
        // 执行业务
        List<Supply> supplyList = supplyService.queryAllSupply();
        // 响应
        return Result.ok(supplyList);
    }

    // 查询所有产地的urL接口/product/pLace-list
    @RequestMapping("/place-list")
    public Result placeList() {
        // 执行业务
        List<Place> placeList = placeService.queryAllPlace();
        // 响应
        return Result.ok(placeList);
    }

    // 查询所有单位的urL接口/product/unit-list
    @RequestMapping("/unit-list")
    public Result unitList() {
        // 执行业务
        List<Unit> unitList = unitService.queryAllUnit();
        // 响应
        return Result.ok(unitList);
    }

    /*
      上传图片的urL接口/product/img-upload

      参数MultipartFile file--表示封装了请求参数名叫file的上传的图片

      file.transferTo（上传的文件保存到的磁盘文件的ile对象）；--实现文件的上传；
    */

    // 将配置文件中file.upload-path属性值注入给控制器的成员属性upLoadPath--图片上传的位置(classpath:static/img/upload)
    @Value("${file.upload-path}")
    private String upLoadPath;

    @CrossOrigin  // 示url接口/product/img-upLoad允许被跨域请求
    @RequestMapping("/img-upload")
    public Result uploadImage(MultipartFile file) {
        try {
            /*
              拿到图片上传到的目录路径的File对象--classpath：static/img/upLoad

              因为图片上传到的目录路径是个类路径（resource下的路径/cLasses下的路径，就是带有前缀classpath)，
              所以不能直接将路径封装到FiLe对象；使用类路径资源工具类ResourceUtiLs的方法getFiLe()来解析类路
              径并拿到封装了类路径的FiLe对象；
            */
            File uploadDirFile = ResourceUtils.getFile(upLoadPath);
            // 拿到图片上传到的目录路径的磁盘路径
            String uploadDirPath = uploadDirFile.getAbsolutePath();
            // 拿到上传的图片的名称
            String filename = file.getOriginalFilename();
            // 拿到上传的文件要保存到的磁盘文件的路径
            String uploadFilePath = uploadDirPath + "\\" + filename;
            // 上传图片
            file.transferTo(new File(uploadFilePath));

            // 成功响应
            return Result.ok("图片上传成功!");
        } catch (Exception e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "图片上传失败!");
        }
    }


    // 添加商品的Url接口/product/product-add
    @RequestMapping("/product-add")
    public Result addProduct(@RequestBody Product product, @RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token) {
        // 拿到当前登录的用户id
        CurrentUser currentUser = tokenUtils.getCurrentUser(token);
        int createBy = currentUser.getUserId();
        product.setCreateBy(createBy);

        // 执行业务
        Result result = productService.saveProduct(product);
        // 响应
        return result;
    }

    // 修改商品上下架状态的urL接口/product/state-change
    @RequestMapping("/state-change")
    public Result changeProductState(@RequestBody Product product) {
        // 执行业务
        Result result = productService.updateStateByPid(product);
        // 响应
        return result;
    }

    // 删除单个商品的urL接口/product/product-delete/{productId}
    @RequestMapping("/product-delete/{productId}")
    public Result deleteProduct(@PathVariable Integer productId) {
        // 执行业务
        Result result = productService.deleteProductByIds(Arrays.asList(productId));
        // 响应
        return result;
    }

    // 批量删除商品的urL接口/product/product-list-delete
    @RequestMapping("/product-list-delete")
    public Result deleteProductList(@RequestBody List<Integer> productIdList) {
        // 执行业务
        Result result = productService.deleteProductByIds(productIdList);
        // 响应
        return result;
    }
}
