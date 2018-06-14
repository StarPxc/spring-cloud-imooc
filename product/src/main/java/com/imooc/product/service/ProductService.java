package com.imooc.product.service;

import com.imooc.product.dataobject.ProductInfo;

import java.util.List;

/**
 * @author 浦希成 【pxc2955317305@outlook.com】
 * @Package com.imooc.product.service
 * @date 2018/6/14  18:57
 * @Description: TODO
 */

public interface ProductService {
    /**
     * 查询所有在架商品
     * @return
     */
    List<ProductInfo> findUpAll();
}
