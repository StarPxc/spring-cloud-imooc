package com.imooc.product.controller;

import com.imooc.product.dataobject.ProductCategory;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.service.CategoryService;
import com.imooc.product.service.ProductService;
import com.imooc.product.utils.ResultVOUtil;
import com.imooc.product.vo.ProductInfoVO;
import com.imooc.product.vo.ProductVO;
import com.imooc.product.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.controller
 * @date 2018/6/14  15:05
 * @Description: TODO
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultVO list(){
        //1.查询所有在架的商品
        List<ProductInfo> productInfoList=productService.findUpAll();
        //2.获取类目type的列表
        List<Integer> categoryTypeList=productInfoList.stream()
                .map(ProductInfo::getCategoryType)
                .collect(Collectors.toList());
        //3.查询类目
        List<ProductCategory> categoryList=categoryService.findByCategoryTypeIn(categoryTypeList);
        //4.构造数据
        List<ProductVO> productVOList=new ArrayList<>();
        for (ProductCategory productCategory: categoryList) {
            ProductVO productVO=new ProductVO();
            productVO.setCategoryName(productCategory.getCategoryName());
            productVO.setCategoryType(productCategory.getCategoryType());
            List<ProductInfoVO> productInfoVOList=new ArrayList<>();
            for (ProductInfo productInfo:productInfoList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO=new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);//拷贝内容
                    productInfoVOList.add(productInfoVO);
                }

            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
            ;
        }

        return ResultVOUtil.success(productVOList);
    }
}
